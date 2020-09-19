package cn.csl.wenjuan.service;

import cn.csl.basics.dao.BasicsAdminMapper;
import cn.csl.basics.dao.BasicsFileMapper;
import cn.csl.basics.entity.BasicsFile;
import cn.csl.basics.model.ResultResponse;
import cn.csl.basics.service.BasicsService;
import cn.csl.basics.util.*;
import cn.csl.basics.util.email.EmailUtil;
import cn.csl.basics.util.file.FileUtil;
import cn.csl.basics.util.file.ZipUtils;
import cn.csl.basics.util.qrcode.QrCodeUtil;
import cn.csl.basics.vo.chart.line.ChartLine;
import cn.csl.manage.dao.SysEnvironmentMapper;
import cn.csl.manage.entity.SysEnvironment;
import cn.csl.manage.redis.JedisUtil;
import cn.csl.wenjuan.dao.*;
import cn.csl.wenjuan.entity.*;
import cn.csl.wenjuan.front.dto.PrescribedDto;
import cn.csl.wenjuan.front.dto.SettingDto;
import cn.csl.wenjuan.front.vo.WjAnswerVo;
import cn.csl.wenjuan.front.vo.WjQuestionVo;
import cn.csl.wenjuan.front.vo.WjReplyVo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.zxing.WriterException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class WjQuestionServive extends BasicsService<WjQuestion> {
	@Autowired
	private JedisUtil jedisUtil;
	@Resource
	private WjFootprintMapper wjFootprintMapper;
	@Resource
	private WjAnswerMapper wjAnswerMapper;
	@Resource
	private WjBannerMapper wjBannerMapper;
	@Resource
	private WjCheckMapper wjCheckMapper;
	@Resource
	private WjReplyMapper wjReplyMapper;
	@Resource
	private WjQuestionMapper wjQuestionMapper;
	@Resource
	private WjProblemMapper wjProblemMapper;
	@Resource
	private WjChoiceMapper wjChoiceMapper;
	@Resource
	private BasicsFileMapper basicsFileMapper;
	@Resource
	private SysEnvironmentMapper sysEnvironmentMapper;
	@Override
	public BasicsAdminMapper<WjQuestion> getBasicsAdminMapper(){return wjQuestionMapper;}

	private void sendMail(WjQuestion question, HttpServletRequest request){
		SysEnvironment sysEnvironment =sysEnvironmentMapper.selectByPrimaryKey(1L);
		String  str="";
		str+="<table>";
		str+="<tr class='text-c'>";
		str+="<th scope='col' colspan='4'>"+question.getName()+"</th>";
		str+="</tr>";
		str+="<tr class='text-c'>";
		str+="<th scope='col' colspan='4'>活动已结束请在系统分析中查看数据分析或扫描二维码下载</th>";
		str+="</tr>";
		str+="<tr class='text-c'>";
		String hostIp = IpUtil.getHostIp();
		String url = "http://" + hostIp + ":" + request.getLocalPort() + request.getContextPath()+question.getZipCode();
		str+="<th scope='col' colspan='4'><img style=\"width: 100%;\" src=\""+url+"\" /></th>";
		str+="</tr>";
		str+="</table>";
		EmailUtil.sendMail(sysEnvironment.getEmailHost(), sysEnvironment.getEmailSender(), sysEnvironment.getName(),
				sysEnvironment.getEmailPassword(),question.getMailbox(), "女神问卷", "活动结束通知",str);
	}

	@Transactional
	public ResultResponse editObtain(String dafenStr,String problemId, HttpServletRequest request){//////打分
		ResultResponse response = new ResultResponse();
		JSONArray dafens = JSON.parseArray(dafenStr);
		WjProblem problem = wjProblemMapper.selectByPrimaryKey(Long.valueOf(problemId));
		WjQuestion question = wjQuestionMapper.selectByPrimaryKey(problem.getQuestionId());

		if(question.getEndTime().after(new Date())){
			response.setFailureResult("活动还没结束不能批阅");
			return response;
		}

		List<String> hang = new ArrayList<>();
		hang.add("用户昵称");
		hang.add("总分");
		List<WjProblem> problems = wjProblemMapper.listByAttribute("questionId",problem.getQuestionId()+"",1);

		JSONArray noIds =  new JSONArray();
		int index = 0;
		for (int i=0;i<problems.size();i++) {
			System.out.println(problems.get(i).getId());
			if(problems.get(i).getId().equals(problem.getId())){
				index = i;
			}else if(problems.get(i).getGenre()==0){
				noIds.add((problems.get(i).getOrderNum()+1));//////其他没有阅卷的题目
			}
			hang.add(problems.get(i).getName());
		}
		Map<String,Object> map = new HashMap<>();
		if(problem.getGenre()==1&&noIds.size()>0){//该题被批阅过 而且还有其他没批阅过的题会有提示让他去批阅、、否则
			map.put("num",noIds.size());
			map.put("mag","还有第"+noIds.toJSONString()+"题等待批阅");
			response.setSuccessResult(map);
			return response;
		}else if(problem.getGenre()==0){//这题没有被皮越过
			for (int i=0;i<dafens.size();i++){
				JSONObject dafen =(JSONObject)dafens.get(i);
				WjAnswer wjAnswer = wjAnswerMapper.selectByPrimaryKey(dafen.getLong("aid"));
				wjAnswer.setObtain(dafen.getInteger("obtain"));
				wjAnswerMapper.updateByPrimaryKey(wjAnswer);
				WjReply wjReply = wjReplyMapper.selectByPrimaryKey(wjAnswer.getReplyId());
				JSONArray wentis = JSON.parseArray(wjReply.getWentisStr());
				JSONObject wenti =(JSONObject)wentis.get(index);
				wenti.remove("obtain");
				wenti.put("obtain",wjAnswer.getObtain());
				wentis.set(index,wenti);
				wjReply.setFraction(wjReply.getFraction()+wjAnswer.getObtain());/////重新计算这个人的总分
				wjReply.setWentisStr(wentis.toJSONString());
				wjReplyMapper.updateByPrimaryKey(wjReply);
				problem.setFraction(problem.getFraction()+wjAnswer.getObtain());////计算该题总得分
			}
			problem.setGenre(1);
			wjProblemMapper.updateByPrimaryKey(problem);//////改为已经阅过卷
		}
		if(noIds.size()>0){//还有没阅卷的题目
			map.put("num",noIds.size());
			map.put("mag","还有第"+noIds.toJSONString()+"题等待批阅");
			response.setSuccessResult(map);
			return response;
		}
		//////////////全部批改完了
		map.put("num",noIds.size());
		try {
			String questionName = question.getName().replaceAll("/","")
					.replaceAll("\\\\","")
					.replaceAll(":","")
					.replaceAll("\\*","")
					.replaceAll("<","")
					.replaceAll(">","")
					.replaceAll("|","")
					.replaceAll("\\?","")
					.replaceAll("？","");
			Map<String,String> idNameMap = new HashMap<>();
			String realPath = request.getSession().getServletContext().getRealPath("/");//系统路径
			String questionPath = realPath+"/download/wenjuan/question/"+question.getId()+"_"+questionName;
			String excelName = questionPath+"/"+questionName+"得分.xls";
			List<List<WjAnswerVo>> userAnswers = new ArrayList<>();
			List<WjAnswerVo> answerVos = wjAnswerMapper.listAnswerVoByQuestionId(question.getId());
			//每一个用户 昵称 用于文件命名
			List<WjAnswerVo> uWjAnswerVo = new ArrayList<>();
			for (WjAnswerVo wjAnswerVo:answerVos) {
				if(uWjAnswerVo.size()==0){//第一个
					uWjAnswerVo.add(wjAnswerVo);
				}else if(uWjAnswerVo.get(uWjAnswerVo.size()-1).getWxUserId()==wjAnswerVo.getWxUserId()){
					uWjAnswerVo.add(wjAnswerVo);
					//现在的额这个跟上个一样
				}else{
					userAnswers.add(uWjAnswerVo);
					uWjAnswerVo = new ArrayList<>();
					uWjAnswerVo.add(wjAnswerVo);
				}
				idNameMap.put(wjAnswerVo.getWxUserId()+"",wjAnswerVo.getNickName());//用户名id========map
			}
			userAnswers.add(uWjAnswerVo);



			//////////////////////////初始化数据数组
			List<List<String>> data = new ArrayList<>();
			hang.add("真实姓名");
			hang.add("手机号");
			hang.add("答题时间");
			data.add(hang);/////题目先进来
			for (int x = 0;x<userAnswers.size();x++){
				hang = new ArrayList<>();
				WjReplyVo replyVo = null;
				if(userAnswers.get(x).size()>0){
					WjAnswerVo wjAnswerVo = userAnswers.get(x).get(0);
					replyVo = wjReplyMapper.selectByReplyVo(wjAnswerVo.getQuestionId(),wjAnswerVo.getWxUserId());
					hang.add(replyVo.getNickName());
					hang.add(replyVo.getFraction()+"");
				}
				for (int y = 0;y<userAnswers.get(x).size();y++){
					hang.add(userAnswers.get(x).get(y).getObtain()+"");
				}
				if(userAnswers.get(x).size()>0){
					hang.add(replyVo.getRealName());
					hang.add(replyVo.getPhone());
					hang.add(DatetimeUtils.dateToString(replyVo.getCreateTime()));
				}
				data.add(hang);
			}
			FileUtil.createFile(excelName);
			ExcelHandle.createExcel(excelName,questionName,data);
			String zipFile = questionPath+".zip";
			FileUtil.createFile(zipFile);
			FileOutputStream fos1 = new FileOutputStream(new File(zipFile));
			ZipUtils.toZip(questionPath, fos1,true);
		}catch (IOException e){
			map.put("mag","批阅完成，生成结果报告异常请联系管理员");
			response.setSuccessResult(map);
			return response;
		}

		map.put("mag","试卷全部批阅完成，下载结果报告即可");
		response.setSuccessResult(map);
		return response;
	}

	public ResultResponse piliang(String examineFlag,String filename,HttpServletRequest request){
		ResultResponse response = new ResultResponse();
		Map<String,Object> remap = new HashMap<>();

		JSONArray jsonArray = new JSONArray();
		Object danxuan = JSONObject.parse("{id:0,questionId:0,name:\"\",orderNum:-1,qType:\"danxuan\",obtain:-1,cover:[],coverId:[],must:1,choicesNunber:0,correctNumner:0,score:1,scoreIndex:0,examineFlag:0,choices:[]}");
		Object duoxuan = JSONObject.parse("{id:0,questionId:0,name:\"\",orderNum:-1,qType:\"duoxuan\",cover:[],obtain:-1,coverId:[],must:1,choicesNunber:0,correctNumner:0,examineFlag:0,score:1,scoreIndex:0,choices:[]}");

		Object jianda = JSONObject.parse("{id:0,questionId:0,name:\"\",cover:[],coverId:[],orderNum:-1,obtain:-1,qType:\"jianda\",must:1,lineNumber:2,score:1,scoreIndex:0,examineFlag:0}");
		Object tiankong = JSONObject.parse("{id:0,questionId:0,name:\"\",qType:\"tiankong\",cover:[],coverId:[],orderNum:-1,obtain:-1,must:1,lineNumber:1,checkType:\"feikong\",reg:\"/^.+$/\",msg:\"不能为空\",score:1,scoreIndex:0,examineFlag:0}");

		Object wenjian = JSONObject.parse("{id:0,questionId:0,name:\"\",orderNum:-1,qType:\"wenjian\",must:1,filePath:\"\",fileId:0,score:1,scoreIndex:0,examineFlag:0,obtain:-1}");
		Object tupian = JSONObject.parse("{id:0,questionId:0,name:\"\",orderNum:-1,qType:\"tupian\",must:1,imgPath:\"\",imgId:0,score:1,scoreIndex:0,examineFlag:0,obtain:-1}");

		Object weizhi = JSONObject.parse("{id:0,questionId:0,name:\"\",cover:[],coverId:[],orderNum:-1,qType:\"position\",must:1,obtain:-1,position:\"\",score:1,scoreIndex:0,examineFlag:0}");
		Object dingwei = JSONObject.parse("{id:0,questionId:0,name:\"\",cover:[],coverId:[],orderNum:-1,obtain:-1,qType:\"location\",must:1,location:\"\",score:1,scoreIndex:0,examineFlag:0}");

		Object pingfen = JSONObject.parse("{id:0,questionId:0,name:\"\",cover:[],coverId:[],orderNum:-1,qType:\"pingfen\",must:1,obtain:-1,maxFen:5,fen:0,score:1,scoreIndex:0,examineFlag:0}");
		Map<String,Integer> map = new HashMap<>();
		jsonArray.add(danxuan);
		map.put("单选题",0);

		jsonArray.add(duoxuan);
		map.put("多选题",1);

		jsonArray.add(jianda);
		map.put("简答题",2);

		jsonArray.add(tiankong);
		map.put("填空题",3);

		jsonArray.add(wenjian);
		map.put("文件题",4);
		jsonArray.add(tupian);
		map.put("图片题",5);
		jsonArray.add(weizhi);
		map.put("位置题",6);
		jsonArray.add(dingwei);
		map.put("定位题",7);
		jsonArray.add(pingfen);
		map.put("评分题",7);
		JSONArray pros = new JSONArray();
		int errorNum = 0;
		int susNum = 0;

		Map<String,Integer> cmap = new HashMap<>();
		List<WjCheck> checks = wjCheckMapper.listByAttribute("state","1",1);
		int ci = 0;
		for (WjCheck check:checks) {
			cmap.put(check.getName(),ci);
			ci++;
		}
		try {
			String realPath = request.getSession().getServletContext().getRealPath("/");//系统路径
			String file = realPath+filename;
			List<List<String>> data = ExcelHandle.readExcel(file);
			int tishu = data.get(0).size();//有几道题
			for (int x=0;x<data.size();x++){
				int cha = tishu-data.get(x).size();
				while (cha>0){
					data.get(x).add("");
					cha--;
				}
			}
			for (int y=0;y<data.get(0).size();y++){
				List<String> tidata = new ArrayList<>();
				for (int x=0;x<data.size();x++){
					tidata.add(data.get(x).get(y));
				}
				JSONObject pro = initProblem(jsonArray,map,tidata,Integer.valueOf(examineFlag),checks,cmap);
				if(pro!=null){
					pros.add(pro);
					susNum++;
				}else{
					errorNum++;
				}
			}
			remap.put("errorNum",errorNum);
			remap.put("susNum",susNum);
			remap.put("wentis",pros.toJSONString());
			response.setSuccessResult(remap);
		}catch (Exception e){
			response.setFailureResult("上传失败");
		}





		return response;
	}
	////////批量上传 单个题目的初始化
	private JSONObject initProblem(JSONArray jsonArray, Map<String,Integer> map,List<String> tidata,int examineFlag,
								   List<WjCheck> checks,Map<String,Integer> cmap ){
		int i = map.get(tidata.get(0));
		if (i>9&&i<0){
			return null;
		}
		try {
			JSONObject pro = (JSONObject)JSONObject.parse(jsonArray.getJSONObject(i).toJSONString());
			pro.put("name",tidata.get(1));
			pro.put("examineFlag",examineFlag);
			if(pro.getString("qType").equals("danxuan")||pro.getString("qType").equals("duoxuan")){//选择题
				JSONArray choices = new JSONArray();
				for (int x=2;x<tidata.size();x++){
					if(!tidata.get(x).equals("")){
						JSONObject choice = (JSONObject)JSONObject.parse("{id:0,cover:[],coverId:[],name:\"\",flag:false}");
						choice.put("name",tidata.get(x));
						choices.add(choice);
					}
				}
				pro.put("choices",choices);
			}else if(pro.getString("qType").equals("pingfen")){////评分体
				int maxfen = Integer.parseInt(tidata.get(2));
				pro.put("maxFen",maxfen);
			}else if(pro.getString("qType").equals("jiandati")){////简答题
				int lineNumber = Integer.parseInt(tidata.get(2));
				pro.put("lineNumber",lineNumber);
			}else if(pro.getString("qType").equals("tiankong")){////填空题
				int ci = cmap.get(tidata.get(2));
				if(ci==-1){ci=1;}
				WjCheck check = checks.get(ci);
				pro.put("checkType",check.getIcon());
				pro.put("msg",check.getMsg());
				pro.put("reg",check.getReg());
			}
			return pro;
		}catch (Exception e){
			return null;
		}
	}


	public ResultResponse changeZipFile(String questionId, HttpServletRequest request){
		ResultResponse response = new ResultResponse();
		WjQuestion question = wjQuestionMapper.selectByPrimaryKey(Long.valueOf(questionId));
		List<WjAnswerVo> answerVos = wjAnswerMapper.listAnswerVoByQuestionId(question.getId());
		Map<String,String> pidNameMap = new HashMap<>();
		Map<String,String> idNameMap = new HashMap<>();
		Map<String,String> idChoiceMap = new HashMap<>();
		List<List<WjAnswerVo>> userAnswers = new ArrayList<>();
		List<WjProblem> problems = wjProblemMapper.listByAttribute("questionId",questionId,1);
		List<WjChoice> choices = new ArrayList<>();
		//文件名称不能是/ \ : *  < > | ？
		String questionName = question.getName().replaceAll("/","")
				.replaceAll("\\\\","")
				.replaceAll(":","")
				.replaceAll("\\*","")
				.replaceAll("<","")
				.replaceAll(">","")
				.replaceAll("|","")
				.replaceAll("\\?","")
				.replaceAll("？","");
		StringBuffer instructions = new StringBuffer(questionName+"的阅读须知\n");
		int i = 1;
		List<String> hang = new ArrayList<>();
		hang.add("用户昵称");


		for (WjProblem wjProblem:problems) {
			choices.addAll(wjChoiceMapper.listByAttribute("problemId",wjProblem.getId()+"",1));
			pidNameMap.put(""+wjProblem.getId(),wjProblem.getName());
			instructions.append("第"+i+"题：\t"+wjProblem.getName()+"\t作答:"+wjProblem.getAnswerNumber()+"人\n");
			if(wjProblem.getqType().equals("tupian")){//图片
				instructions.append("第"+i+"题：\t"+wjProblem.getName()+"\t作答:"+wjProblem.getAnswerNumber()+"人\t该题图片存放与"+wjProblem.getId()+"文件夹下\n");
			}else if(wjProblem.getqType().equals("wenjian")){//文件
				instructions.append("第"+i+"题：\t"+wjProblem.getName()+"\t作答:"+wjProblem.getAnswerNumber()+"人\t该题文件存放与"+wjProblem.getId()+"文件夹下\n");
			}
			i += 1;
			hang.add(wjProblem.getName());
		}
		for (WjChoice choice:choices) {
			idChoiceMap.put(choice.getId()+"",choice.getName());
		}
		try {
			String realPath = request.getSession().getServletContext().getRealPath("/");//系统路径
			String questionPath = realPath+"/download/wenjuan/question/"+question.getId()+"_"+questionName;
			String zipFileUrl = "/download/wenjuan/question/"+question.getId()+"_"+questionName+".zip";
			FileUtil.makeDir(questionPath);
			FileUtil.StringBuffer(questionPath+"\\阅读须知.txt",instructions.toString());/////////生成阅读须知
			//每一个用户 昵称 用于文件命名
			List<WjAnswerVo> uWjAnswerVo = new ArrayList<>();
			for (WjAnswerVo wjAnswerVo:answerVos) {
				if(wjAnswerVo.getqType().equals("tupian")){//图片//////////////////////////////////拷贝文件
					String src = realPath+wjAnswerVo.getImgPath();
					String suffix = wjAnswerVo.getImgPath().substring(wjAnswerVo.getImgPath().lastIndexOf("."),wjAnswerVo.getImgPath().length());
					String dest = questionPath+"/"+wjAnswerVo.getProblemId();
					FileUtil.makeDir(dest);
					dest+="/"+wjAnswerVo.getNickName()+suffix;
					FileUtil.copyFile(src,dest);
				}else if(wjAnswerVo.getqType().equals("wenjian")){//文件//////////////////////////////////拷贝文件
					String src = realPath+wjAnswerVo.getFilePath();
					String suffix = wjAnswerVo.getFilePath().substring(wjAnswerVo.getFilePath().lastIndexOf("."),wjAnswerVo.getFilePath().length());
					String dest = questionPath+"/"+wjAnswerVo.getProblemId();
					FileUtil.makeDir(dest);
					dest+="/"+wjAnswerVo.getNickName()+suffix;
					FileUtil.copyFile(src,dest);
				}
				if(uWjAnswerVo.size()==0){//第一个
					uWjAnswerVo.add(wjAnswerVo);
				}else if(uWjAnswerVo.get(uWjAnswerVo.size()-1).getWxUserId()==wjAnswerVo.getWxUserId()){
					//现在的额这个跟上个一样
					uWjAnswerVo.add(wjAnswerVo);
				}else{
					userAnswers.add(uWjAnswerVo);
					uWjAnswerVo = new ArrayList<>();
					uWjAnswerVo.add(wjAnswerVo);
				}
				idNameMap.put(wjAnswerVo.getWxUserId()+"",wjAnswerVo.getNickName());//用户名id========map
			}
			userAnswers.add(uWjAnswerVo);
			//////////////////////////初始化数据数组
			List<List<String>> data = new ArrayList<>();

			hang.add("真实姓名");
			hang.add("手机号");
			hang.add("答题时间");
			if(question.getRestrictFlag()==1){
				hang.add("ip地址");
				hang.add("经度");
				hang.add("纬度");
				hang.add("省");
				hang.add("市");
				hang.add("区县");
				hang.add("街道");
			}
			data.add(hang);/////题目先进来
			for (int x = 0;x<userAnswers.size();x++){
				hang = new ArrayList<>();
				WjReplyVo replyVo = null;
				if(userAnswers.get(x).size()>0){
					WjAnswerVo wjAnswerVo = userAnswers.get(x).get(0);
					replyVo = wjReplyMapper.selectByReplyVo(wjAnswerVo.getQuestionId(),wjAnswerVo.getWxUserId());
					hang.add(replyVo.getNickName());
				}
				for (int y = 0;y<userAnswers.get(x).size();y++){
					hang.add(getAnswerStr(userAnswers.get(x).get(y),idNameMap,idChoiceMap));
				}
				if(userAnswers.get(x).size()>0){
					hang.add(replyVo.getRealName());
					hang.add(replyVo.getPhone());
					hang.add(DatetimeUtils.dateToString(replyVo.getCreateTime()));
					if(question.getRestrictFlag()==1){
						hang.add(replyVo.getIpAddress());
						hang.add(replyVo.getLongitude());
						hang.add(replyVo.getLatitude());
						hang.add(replyVo.getProvince());
						hang.add(replyVo.getCity());
						hang.add(replyVo.getDistrict());
						hang.add(replyVo.getStreet());
					}
				}
				data.add(hang);
			}

			System.out.println("-------");
			String excelName = questionPath+"/"+questionName+".xls";
			FileUtil.createFile(excelName);
			//////////////////////////生成EXCLE
			ExcelHandle.createExcel(excelName,questionName,data);
			String zipFile = questionPath+".zip";
			FileUtil.createFile(zipFile);
			FileOutputStream fos1 = new FileOutputStream(new File(zipFile));
			ZipUtils.toZip(questionPath, fos1,true);

			String hostIp = IpUtil.getHostIp();
			String qrCodePath = request.getSession().getServletContext().getRealPath("/")+"/static/img/wenjuan/question/qrcode";
			File file = new File(qrCodePath);
			if(!file.exists()){
				file.mkdirs();
			}
			FileUtil.createFile(qrCodePath+"/"+questionId+"_"+questionName+".png");
			String qrCodeUrl = "http://" + hostIp + ":" + request.getLocalPort() + request.getContextPath()
					+ zipFileUrl;
			QrCodeUtil.makeQRCodeImg(qrCodeUrl, "png", 200, 200, qrCodePath+"/"+ questionId+"_"+questionName);
			question.setZipCode("/static/img/wenjuan/question/qrcode"+"/"+questionId+"_"+questionName+".png");
			question.setZipPath(zipFileUrl);
			wjQuestionMapper.updateByPrimaryKey(question);
		}catch (IOException io){
			io.printStackTrace();
		}catch (WriterException e){
			e.printStackTrace();
		}
		if (question.getSendFlag()==1){
			sendMail(question,request);
		}
		response.setSuccessResult("chenglle");
		return response;
	}
	private String getAnswerStr(WjAnswerVo answer,Map<String,String> idNameMap,Map<String,String> idChoiceMap){
		if(answer.getqType().equals("pingfen")){
			return String.valueOf(answer.getFen());
		}else if(answer.getqType().equals("duoxuan")||answer.getqType().equals("danxuan")){
			StringBuffer reb = new StringBuffer("");
			JSONArray ids = JSONObject.parseArray(answer.getContent());
			for (int i=0;i<ids.size();i++){
				reb.append(idChoiceMap.get(String.valueOf(ids.get(i)))+"、");
			}
			String re = reb.toString();
			if(ids.size()>0){
				re = re.substring(0,re.length()-1);
			}
			return re;
		}else if(answer.getqType().equals("wenjian")){
			String suffix = answer.getFilePath().substring(answer.getFilePath().lastIndexOf("."),answer.getFilePath().length());
			return answer.getProblemId()+"\\"+idNameMap.get(answer.getWxUserId()+"")+suffix;
		}else if(answer.getqType().equals("tupian")){
			String suffix = answer.getImgPath().substring(answer.getImgPath().lastIndexOf("."),answer.getImgPath().length());
			return answer.getProblemId()+"\\"+idNameMap.get(answer.getWxUserId()+"")+suffix;
		}else if(answer.getqType().equals("location")){//定位
			return "纬度:"+answer.getLatitude()+"、经度:"+answer.getLongitude()+"、位置:"+answer.getAddressDesc();
		}else if(answer.getqType().equals("position")){//位置
			return "纬度:"+answer.getLatitude()+"、经度:"+answer.getLongitude()+"、位置:"+answer.getAddress();
		}else {
			return answer.getContent();
		}
	}
	@Transactional//作答
	public ResultResponse editReply(String wentisStr,String questionId, PrescribedDto prescribedDto){
		ResultResponse resultResponse = new ResultResponse();
		WjQuestion question = wjQuestionMapper.selectByPrimaryKey(Long.valueOf(questionId));
		if(question.getRestrictFlag()==1){//不限制地区的问卷不用处理
			prescribedDto.setCity(prescribedDto.getCity().replace("市",""));
			prescribedDto.setProvince(prescribedDto.getProvince().replace("市",""));
			prescribedDto.setProvince(prescribedDto.getProvince().replace("省",""));
		}
		//看公开情况
		if(question.getOvert()!=1&&!question.getVoteCode().equals(prescribedDto.getCode())){//不为1就是不公开 还不相等
			resultResponse.setFailureResult("活动密码错误");
			return resultResponse;
		}
		if(question.getSuspend()==0){//0暂停
			resultResponse.setFailureResult("问卷暂停回答");//返回去
			return resultResponse;
		}
		if(new Date().before(question.getStartTime())){
			resultResponse.setFailureResult("问卷还没开始呢");//返回去
			return resultResponse;
		}
		if(new Date().after(question.getEndTime())){
			resultResponse.setFailureResult("问卷已经结束了");//返回去
			return resultResponse;
		}
		if(question.getRestrictFlag()==1){/////////////地区限制
			//看地区
			String province = question.getProvince();
			String city = question.getCity();
			String district = question.getDistrict();
			if (!province.equals(prescribedDto.getProvince())){
				resultResponse.setFailureResult("活动仅限:"+province+"-"+city+"-"+district);
				return resultResponse;
			}
			//城市不是不限且 和 当前城市不同
			if ((!"不限".equals(city))&&(!city.equals(prescribedDto.getCity()))){
				resultResponse.setFailureResult("活动仅限:"+province+"-"+city+"-"+district);
				return resultResponse;
			}
			//区县不是不限且 和 当前区县不同
			if ((!"不限".equals(district))&&(!district.equals(prescribedDto.getDistrict()))){
				resultResponse.setFailureResult("活动仅限:"+province+"-"+city+"-"+district);
				return resultResponse;
			}

//			//ip限制
			String sql = "and questionId = "+questionId+" and ipAddress='"+prescribedDto.getIpAddress()+"'";
			List<WjReply> wjReplies = wjReplyMapper.listByAttrValueMap(sql,1);
			if(wjReplies.size()>=question.getIpWxUserFrequency()){
				resultResponse.setFailureResult("该活动限制每个IP不超过"+question.getIpWxUserFrequency()+"个微信用户");
				return resultResponse;
			}
		}
		String sql = "and questionId = "+questionId+" and wxUserId="+prescribedDto.getWxUserId();
		List<WjReply> wjReplies = wjReplyMapper.listByAttrValueMap(sql,1);
		if(wjReplies.size()!=0){///////////之前答过
			resultResponse.setFailureResult("您已经答过了");//返回去
			return resultResponse;
		}
		WjReply wjReply = new WjReply();///////////////设置这些信息
		try {
			BeanUtils.copyProperties(wjReply, prescribedDto);//
		}catch (Exception e){
		}
		wjReply.setExamineFlag(question.getExamineFlag());
		wjReply.setState(1);
		wjReply.setQuestionId(Long.valueOf(questionId));
		wjReply.setCreateTime(new Date());
		wjReply.setWentisStr(wentisStr);
		wjReply.setFraction(0);
		wjReplyMapper.insert(wjReply);
		List<WjAnswer> wjAnswers = getAnswers(wentisStr,wjReply);
		int index=0;
		JSONArray wentisArray = JSONArray.parseArray(wentisStr);
		for (WjAnswer answer:wjAnswers) {
			WjProblem problem = wjProblemMapper.selectByPrimaryKey(answer.getProblemId());//////////用来计算单分数
			if(question.getExamineFlag()==1){/////////试卷
				answer.setScore(problem.getScore());//满分
			}
			if("danxuan".equals(answer.getqType())||"duoxuan".equals(answer.getqType())){//选择题要去设置选项人数
				JSONArray ids = JSONArray.parseArray(answer.getContent());
				for (int y=0;y<ids.size();y++){
					WjChoice choice = wjChoiceMapper.selectByPrimaryKey(ids.getLong(y));
					choice.setSelectNum(choice.getSelectNum()+1);//////////////该选项选择人数
					wjChoiceMapper.updateByPrimaryKey(choice);
				}
			}

			answer.setObtain(countScore(answer,problem));
			problem.setAnswerNumber(problem.getAnswerNumber()+1);
			if(problem.getExamineFlag()==1){///////计算下分数
				if(answer.getObtain()>0){//////判了分的再给题目总得分加分
					problem.setFraction(problem.getFraction()+answer.getObtain());
				}
			}else{
				problem.setFraction(0);
			}
			wjProblemMapper.updateByPrimaryKey(problem);//////////////问题回答人数
			wjAnswerMapper.insert(answer);
			JSONObject wenti = (JSONObject)wentisArray.get(index);
			wenti.remove("obtain");
			wenti.put("obtain",answer.getObtain());
			if(question.getExamineFlag()==1&&answer.getObtain()>0){//考试
				wjReply.setFraction(answer.getObtain()+wjReply.getFraction());
			}
			wentisArray.set(index,wenti);
			index++;
		}
		question.setAnswerNumber(question.getAnswerNumber()+1);
		wjQuestionMapper.updateByPrimaryKey(question);//、、、、问卷作答人数
		wjReply.setWentisStr(wentisArray.toJSONString());//////更新进入用户的分数
		wjReplyMapper.updateByPrimaryKey(wjReply);
		resultResponse.setSuccessResult(wjReply.getId());//返回去
//		resultResponse.setSuccessResult(1);//返回去
		return resultResponse;
	}
	private int countScore(WjAnswer answer,WjProblem problem){
		if(problem.getExamineFlag()==null||problem.getExamineFlag()==0){
			return -2;//问卷类型 分数为-2
		}
		if (answer.getqType().equals("danxuan")||answer.getqType().equals("duoxuan")){///选择题评分呀
			if(problem.getAnswerId().equals(answer.getContent())){//比较答案
				return problem.getScore();
			}else{
				return 0;
			}
		}else{//其他题型需要自己去批阅
			return -1;
		}
	}
	private List<WjAnswer> getAnswers(String wentisStr,WjReply reply){
		List<WjAnswer> wjAnswers = new ArrayList<>();
		JSONArray jsonArray = JSONArray.parseArray(wentisStr);
		for (int x=0;x<jsonArray.size();x++){
			WjAnswer answer = new WjAnswer();
			JSONObject jsonAnswer=jsonArray.getJSONObject(x);
			answer.setState(1);
			answer.setCreateTime(new Date());
			answer.setqType(jsonAnswer.getString("qType"));
			answer.setWxUserId(reply.getWxUserId());
			answer.setReplyId(reply.getId());
			answer.setQuestionId(jsonAnswer.getLong("questionId"));
			answer.setProblemId(jsonAnswer.getLong("id"));
			answer.setOrderNum(jsonAnswer.getInteger("orderNum"));
			//定位题
			if ("location".equals(answer.getqType())){
				JSONObject location = jsonAnswer.getJSONObject("location");
				answer.setLatitude(location.getString("latitude"));
				answer.setLongitude(location.getString("longitude"));
				answer.setAddress(location.getString("address"));
				JSONArray markers = location.getJSONArray("markers");
				if(markers!=null&&markers.size()>0){
					JSONObject marker = markers.getJSONObject(0);
					answer.setAddressDesc(marker.getString("desc"));
				}
				wjAnswers.add(answer);
				continue;
			}
			//位置题
			if ("position".equals(answer.getqType())){
				JSONObject position = jsonAnswer.getJSONObject("position");
				answer.setLatitude(position.getString("latitude"));
				answer.setLongitude(position.getString("longitude"));
				answer.setAddress(position.getString("address"));
				wjAnswers.add(answer);
				continue;
			}
			//文件题
			if ("wenjian".equals(answer.getqType())){
				answer.setFileId(jsonAnswer.getLong("fileId"));
				answer.setFilePath(jsonAnswer.getString("filePath"));
				wjAnswers.add(answer);
				continue;
			}
			//图片题
			if ("tupian".equals(answer.getqType())){
				answer.setImgId(jsonAnswer.getLong("imgId"));
				answer.setImgPath(jsonAnswer.getString("imgPath"));
				wjAnswers.add(answer);
				continue;
			}
			//文本题
			if ("jianda".equals(answer.getqType())||"tiankong".equals(answer.getqType())){
				answer.setContent(jsonAnswer.getString("answer"));
				answer.setCheckId(jsonAnswer.getLong("checkId"));//验证方式id
				answer.setReg(jsonAnswer.getString("reg"));//验证方式id
				wjAnswers.add(answer);
				continue;
			}
			//pingfen题
			if ("pingfen".equals(answer.getqType())){
				answer.setFen(jsonAnswer.getInteger("fen"));
				wjAnswers.add(answer);
				continue;
			}
			//选择题
			if ("danxuan".equals(answer.getqType())||"duoxuan".equals(answer.getqType())){
				JSONArray jsonChoices = jsonAnswer.getJSONArray("choices");
				String content = "";
				for (int y=0;y<jsonChoices.size();y++){
					JSONObject choice = jsonChoices.getJSONObject(y);
					if(choice.getBoolean("flag")){
						content+=","+choice.getString("id");//选项id
					}
				}
				if(content==""){
					answer.setContent("[]");
				}else {
					answer.setContent("[" + content.substring(1, content.length()) + "]");
				}
				wjAnswers.add(answer);
				continue;
			}
		}
		return wjAnswers;
	}
	@Transactional
	public ResultResponse addSave(String settingStr,String wentisStr,String wxUserId,HttpServletRequest request){
		ResultResponse resultResponse = new ResultResponse();
		SettingDto settingDto = getSetting(settingStr);
		WjQuestion question = settingDto.getQuestion();
		JSONArray wentisData = JSONObject.parseArray(wentisStr);
		question.setProblemNumber(wentisData.size());
		question.setVoteCode(RandomUtil.getUUID().substring(0,6));
		question.setWxUserId(Long.valueOf(wxUserId));
		question.setSuspend(1);//不暂停
		boolean copy = false;
		if(question.getId()!=null){
			question.setId(0L);
			copy = true;
		}
		wjQuestionMapper.insert(question);
		if(!copy){//非拷贝 才需要更新
			setFile(question.getCover(),question.getId(),"wj_question","cover");
		}
		List<WjProblem> problems = new ArrayList<>();
		for (int i=0;i<wentisData.size();i++) {
			WjProblem problem = getWjProblem(wentisData.get(i).toString());
			problem.setQuestionId(question.getId());
			problem.setOrderNum(i);
			problem.setState(1);
			problem.setWxUserId(Long.valueOf(wxUserId));
			problem.setExamineFlag(question.getExamineFlag());
			question.setSumScore(question.getSumScore()+problem.getScore());/////计算试卷总分
			problem.setFraction(0);//总分置零
			if(problem.getId()!=null){/////拷贝的id要清零
				problem.setId(0L);
				copy = true;
			}
			wjProblemMapper.insert(problem);
			if(!copy){//非拷贝 才需要更新
				setFile(problem.getCover(),problem.getId(),"wj_problem","cover");
			}
			List<WjChoice> choices = problem.getChoices();
			int index = 1;
			JSONArray answerId = new JSONArray();
			if(choices!=null){
				for (int y=0;y<choices.size();y++) {
					WjChoice choice = choices.get(y);
					choice.setProblemId(problem.getId());
					choice.setState(1);
					choice.setOrderNum(index);
					choice.setSelectNum(0);
					if(question.getExamineFlag()==0){//问卷型不用答案
						choice.setFlag(0);
					}
					if(choice.getId()!=null){/////拷贝的id要清零
						choice.setId(0L);
						copy = true;
					}
					wjChoiceMapper.insert(choice);
					if(choice.getFlag()==1){//答案
						answerId.add(choice.getId());
					}
					if(!copy){//非拷贝 才需要更新
						setFile(choice.getCover(),choice.getId(),"wj_choice","cover");
					}
					choices.set(y,choice);
					index++;
				}
			}
			problem.setAnswerId(answerId.toJSONString());///////保存答案id
			wjProblemMapper.updateByPrimaryKey(problem);
			problems.add(problem);
		}
		String problemsStr = getProblemsStr(wentisStr,problems,1);//要清除答案
		String cproblemsStr = getProblemsStr(wentisStr,problems,0);//bu需要清除答案
		String questionDataStr = settingDto.getSettingStr(settingStr,question);
		question.setProblemsStr(problemsStr);
		question.setCproblemsStr(cproblemsStr);
		question.setQuestionDataStr(questionDataStr);
		wjQuestionMapper.updateByPrimaryKey(question);
		setTimer(question,request);//定时
		resultResponse.setSuccessResult(question);//返回去
		return resultResponse;
	}
	private void setFile(String idsStr,Long surfaceId,String surface,String nature){
		JSONArray ids = JSONArray.parseArray(idsStr);
		if(ids!=null){
			for (int i=0;i<ids.size();i++){
				BasicsFile file = basicsFileMapper.selectByPrimaryKey(ids.getLong(i));
				file.setSurfaceId(surfaceId);
				file.setSurface(surface);
				file.setNature(nature);
				basicsFileMapper.updateByPrimaryKey(file);
			}
		}
	}
	private SettingDto getSetting(String settingStr){
		return new SettingDto(settingStr);
	}
	private WjProblem getWjProblem(String problemStr){
		WjProblem problem = JSONObject.parseObject(problemStr, WjProblem.class);
		problem.setCover(JSONObject.parseObject(problemStr).getString("coverId"));
		problem.setAnswerNumber(0);
		if(problem.getqType().equals("danxuan")||problem.getqType().equals("duoxuan")){
			List<WjChoice> rchoices = getWjChoices(problemStr.toString());
			problem.setGenre(1);/////选择题题目  默认已经阅过卷
			if(problem.getExamineFlag()==0){//非考试问卷要分数清0
				problem.setGenre(0);////////问卷的选择题题目  没有阅过卷
				problem.setScore(0);
			}
			problem.setChoicesNunber(rchoices.size());
			problem.setChoices(rchoices);
		}else{
			problem.setGenre(0);////////其他类型题目  没有阅过卷
		}
		problem.setFraction(0);
		return problem;
	}
	private List<WjChoice> getWjChoices(String problemStr){
		JSONArray choices = JSONObject.parseObject(problemStr).getJSONArray("choices");
		List<WjChoice> rchoices = new ArrayList<>();
		for (int i=0;i<choices.size();i++) {
			WjChoice choice = JSONObject.parseObject(choices.get(i).toString(), WjChoice.class);
			choice.setCover(JSONObject.parseObject(choices.get(i).toString()).getString("coverId"));
			if (JSONObject.parseObject(choices.get(i).toString()).getBoolean("flag")){
				choice.setFlag(1);
			}else {
				choice.setFlag(0);
			}
			rchoices.add(choice);
		}
		return rchoices;
	}
	private String  getProblemsStr(String wentisStr,List<WjProblem> problems,int flag){
		JSONArray wentisData = JSONObject.parseArray(wentisStr);
		for (int i=0;i<problems.size();i++) {
			JSONObject problem = (JSONObject)wentisData.get(i);
			problem.put("id",problems.get(i).getId());
			problem.put("questionId",problems.get(i).getQuestionId());
			problem.put("state",problems.get(i).getState());
			problem.put("wxUserId",problems.get(i).getWxUserId());
			if(problems.get(i).getqType().equals("danxuan")||problems.get(i).getqType().equals("duoxuan")){
				JSONArray choices = problem.getJSONArray("choices");
				for (int y=0;y<choices.size();y++){
					JSONObject choice = (JSONObject)choices.get(y);
					choice.put("id",problems.get(i).getChoices().get(y).getId());
					choice.put("problemId",problems.get(i).getChoices().get(y).getProblemId());
					if(flag==1){
						choice.put("flag",false);//把答案清除
					}
					choices.set(y,choice);
				}
				problem.put("choices",choices);
			}
			wentisData.set(i,problem);
		}
		return wentisData.toJSONString();
	}
	public ResultResponse aopQuestionVo(String id,String wxUserId){
		ResultResponse response = new ResultResponse();
		WjQuestion question = super.selectByPrimaryKey(id);
		String sql = "and questionId = "+id+" and wxUserId="+wxUserId;
		List<WjReply> wjReplies = wjReplyMapper.listByAttrValueMap(sql,1);
		if(wjReplies.size()!=0){///////////之前有过回答
			response.setSuccessResult(new WjQuestionVo(question,wjReplies.get(0).getWentisStr(),1,wjReplies.get(0).getFraction()));
		}else{
			response.setSuccessResult(new WjQuestionVo(question,question.getProblemsStr(),0,0));
		}
		//加入浏览历史
		sql = "and surface = 'wj_question' and subscriberSurface = 'wx_user' and surfaceId="+id+" and subscriberId="+wxUserId;
		List<WjFootprint> wjFootprints = wjFootprintMapper.listByAttrValueMap(sql,1);
		if(wjFootprints.size()>0){//浏览过
			WjFootprint footprint = wjFootprints.get(0);
			footprint.setUpdataTime(new Date());
			wjFootprintMapper.updateByPrimaryKey(footprint);
		}else {
			WjFootprint footprint = new WjFootprint();
			footprint.setCreateTime(new Date());
			footprint.setUpdataTime(new Date());
			footprint.setState(1);
			footprint.setSubscriberId(Long.valueOf(wxUserId));
			footprint.setSubscriberSurface("wx_user");
			footprint.setSurface("wj_question");
			footprint.setSurfaceId(Long.valueOf(id));
			wjFootprintMapper.insert(footprint);
		}
		return response;
	}
	public List<WjQuestionVo> joinQuestionVo(String wxUserId){/////参加的
		List<WjQuestionVo> wjQuestionVos = new ArrayList<>();
		List<WjQuestion> questions = wjQuestionMapper.listJoin(Long.valueOf(wxUserId));
		for (WjQuestion question:questions){
			wjQuestionVos.add(new WjQuestionVo(question));
		}
		return wjQuestionVos;
	}
	public List<WjQuestionVo> footprintQuestionVo(String wxUserId){/////浏览历史的
		List<WjQuestionVo> wjQuestionVos = new ArrayList<>();
		List<WjQuestion> questions = wjQuestionMapper.listFootprint(Long.valueOf(wxUserId));
		for (WjQuestion question:questions){
			wjQuestionVos.add(new WjQuestionVo(question));
		}
		return wjQuestionVos;
	}
	public List<WjQuestionVo> listQuestionVoByWxUserId(String wxUserId){
		List<WjQuestionVo> wjQuestionVos = new ArrayList<>();
		List<WjQuestion> questions = wjQuestionMapper.listByAttribute("wxUserId",wxUserId,1);
		for (WjQuestion question:questions){
			wjQuestionVos.add(new WjQuestionVo(question));
		}
		return wjQuestionVos;
	}
	public List<WjQuestionVo> listQuestionVo(){
		List<WjQuestionVo> wjQuestionVos = new ArrayList<>();
//		List<WjQuestion> questions = wjQuestionMapper.listByAttribute("wxUserId",wxUserId,1);
//		for (WjQuestion question:questions){
//			wjQuestionVos.add(new WjQuestionVo(question));
//		}
		return wjQuestionVos;
	}
	public ResultResponse editSave2(WjQuestion obj,HttpServletRequest request){
		ResultResponse resultResponse = new ResultResponse();
		WjQuestion dbObj = getBasicsAdminMapper().selectByPrimaryKey(obj.getId());
		if(dbObj.getAnswerNumber()>0){//有人回答过了
			resultResponse.setFailureResult("有人回答过了不能修改");
			return resultResponse;
		}
		dbObj.setQuestionDataStr(obj.getQuestionDataStr());
		dbObj.setStartTime(obj.getStartTime());
		dbObj.setEndTime(obj.getEndTime());
		dbObj.setIntroduce(obj.getIntroduce());
		dbObj.setName(obj.getName());
		dbObj.setOvert(obj.getOvert());
		getBasicsAdminMapper().updateByPrimaryKey(dbObj);
		setTimer(dbObj,request);//定时
		resultResponse.setSuccessResult("修改成功");
		return resultResponse;
	}
	private void setTimer(WjQuestion question,HttpServletRequest request){
		Date now = new Date();//现在
		Date endTime = question.getEndTime();//用户输入的结束时间
		Date timer = new Date(endTime.getTime()+Long.valueOf(1000*60*1));//加1分钟
		int time = Integer.parseInt(String.valueOf((timer.getTime()-now.getTime())/(1000)));//换算成秒
		String key = "Timer_wenjuan_question_"+question.getId();
//        boolean flag = jedisUtil.KEYS.exists(key);
		jedisUtil.STRINGS.set(key,"定时器wenjuan_question_"+question.getId());
		jedisUtil.expire(key,time);
		String hostIp = IpUtil.getHostIp();
		String hostUrl = "http://" + hostIp + ":" + request.getLocalPort() + request.getContextPath() + "/wenjuan/front/question/zipFile?questionId="+question.getId();
		jedisUtil.STRINGS.set(key+"_url",hostUrl);
	}
	public ResultResponse changeSuspend(Long id){
		ResultResponse resultResponse = new ResultResponse();
		WjQuestion dbObj = getBasicsAdminMapper().selectByPrimaryKey(id);
		dbObj.setSuspend((dbObj.getSuspend()+1)%2);
		getBasicsAdminMapper().updateByPrimaryKey(dbObj);
		if(dbObj.getSuspend()==1){
			resultResponse.setSuccessResult("已开始");
		}else{
			resultResponse.setSuccessResult("已暂停");
		}
		return resultResponse;
	}
	public ResultResponse analysis(String id){
		ResultResponse response = new ResultResponse();
		WjQuestion question = wjQuestionMapper.selectByPrimaryKey(Long.valueOf(id));
		String frotmt = "'%Y-%m-%d'";
		Long cha = question.getEndTime().getTime()-question.getStartTime().getTime();

//		cha = cha/1000;//毫秒转秒
		if(cha>=365*24*60*Long.valueOf(60*1000)){//大于一年按月
			frotmt= "'%Y年%m月'";
		}else if(cha>=160*24*60*Long.valueOf(60*1000)){//大于5个月按月
			frotmt= "'%m月'";
		}else if(cha>=Long.valueOf(30*24*60)*Long.valueOf(60*1000)){//大于1个月按周
			frotmt= "'%u周'";
		}else if(cha>=7*24*60*Long.valueOf(60*1000)){//大于一周按天
			frotmt= "'%d日'";
		}else if(cha>=24*60*Long.valueOf(60*1000)){//大于一天按小时
			frotmt= "'%d日%k时'";
		}else {//小于一天按分钟
			frotmt= "'%k时%i分'";
		}
		List<WjReplyVo> replyVos = wjReplyMapper.listReplyVo(Long.valueOf(id));
		List<ChartLine> chartLines = wjReplyMapper.listChartLine(frotmt,Long.valueOf(id));
		List<WjProblem> problems = wjProblemMapper.listByAttribute("questionId",id,1);
		List<WjProblem> rProblems = new ArrayList<>();
		for (int i=0;i<problems.size();i++){
			WjProblem problem = problems.get(i);
			List<WjChoice> choices = wjChoiceMapper.listByAttribute("problemId",String.valueOf(problem.getId()),1);
			problem.setChoices(choices);
			rProblems.add(problem);
		}
		Long now = new Date().getTime();
		String lv = String.valueOf((now - question.getStartTime().getTime()) /Float.valueOf(cha)*100);
		if(now - question.getStartTime().getTime()>=cha){
			lv = "100";
		}else{
			lv= lv.substring(0,lv.indexOf(".")+2);
		}
		Map<String,Object> map = new HashMap<>();
		map.put("lv",lv);
		map.put("replyVos",replyVos);
		map.put("question",question);
		map.put("problems",rProblems);
		map.put("chartLines",chartLines);

		response.setSuccessResult(map);
		return response;
	}
	public ResultResponse listAnswerVoByProblemId(String problemId){
		ResultResponse resultResponse = new ResultResponse();
		WjProblem problem = wjProblemMapper.selectByPrimaryKey(Long.valueOf(problemId));
		List<WjAnswerVo> answerVos = wjAnswerMapper.listAnswerVoByProblemId(Long.valueOf(problemId));
		if(problem.getqType().equals("wenjian")){////////文件类的把名字替换了
			for (int i=0;i<answerVos.size();i++){
				WjAnswerVo answerVo = answerVos.get(i);
				answerVo.setFilePath(answerVo.getNickName()+answerVo.getFilePath().substring(answerVo.getFilePath().indexOf("."),answerVo.getFilePath().length()));
			}
		}
		resultResponse.setSuccessResult(answerVos);
		return resultResponse;
	}
	@Override
	public ResultResponse editSave(WjQuestion obj){
		ResultResponse resultResponse = new ResultResponse();
		resultResponse.setSuccessResult("修改成功");
		return resultResponse;
	}
	@Transactional
	public ResultResponse delete(String id){
		ResultResponse response = new ResultResponse();
		wjQuestionMapper.delete(Long.valueOf(id));//删除问卷
		List<WjProblem> problems = wjProblemMapper.listByAttribute("questionId",id,1);
		wjProblemMapper.deleteByQuestionId(Long.valueOf(id));//删除题目
		for (WjProblem problem:problems){

			wjChoiceMapper.deleteByProblemId(problem.getId());//删除选项
		}
		wjReplyMapper.deleteByQuestionId(Long.valueOf(id));//删除答题记录
		wjAnswerMapper.deleteByQuestionId(Long.valueOf(id));//删除答题答案
		response.setSuccessResult("删除成功");
		return response;
	}
	public TableReturnUtil listJson2(TableUploadUtil tableUploadUtil, int banner) throws Exception{
		TableReturnUtil tableReturnUtil = new TableReturnUtil();
		long totalCount = this.getBasicsAdminMapper().getCount(tableUploadUtil).longValue();
		tableReturnUtil.setRecordsFiltered(totalCount);
		tableReturnUtil.setRecordsTotal(totalCount);
		Long pageNum = totalCount/tableUploadUtil.getLength();
		if(totalCount%tableUploadUtil.getLength()!=0){
			pageNum++;
		}
		tableReturnUtil.setPageNum(pageNum);
		List<WjQuestion> questions = this.getBasicsAdminMapper().listPageObjs(tableUploadUtil);
		List<WjQuestionVo> questionVos = new ArrayList<>();
		for (WjQuestion question:questions){
			questionVos.add(new WjQuestionVo(question));
		}
		Map<String,Object> map = new HashMap<>();
		map.put("questionVos",questionVos);
		if(banner==1){
			List<WjBanner> banners = wjBannerMapper.listBanners("wenjuan");
			map.put("banners",banners);
		}

		tableReturnUtil.setData(map);
		return tableReturnUtil;
	}
}
