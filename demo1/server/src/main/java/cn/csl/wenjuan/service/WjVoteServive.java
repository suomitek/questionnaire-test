package cn.csl.wenjuan.service;

import cn.csl.basics.dao.BasicsAdminMapper;
import cn.csl.basics.dao.BasicsFileMapper;
import cn.csl.basics.entity.BasicsFile;
import cn.csl.basics.model.ResultResponse;
import cn.csl.basics.service.BasicsService;
import cn.csl.basics.util.DatetimeUtils;
import cn.csl.basics.util.RandomUtil;
import cn.csl.basics.util.TableReturnUtil;
import cn.csl.basics.util.TableUploadUtil;
import cn.csl.manage.entity.SysUser;
import cn.csl.wenjuan.dao.*;
import cn.csl.wenjuan.entity.*;
import cn.csl.wenjuan.front.vo.WjCandidateVo;
import cn.csl.wenjuan.front.vo.WjQuestionVo;
import cn.csl.wenjuan.front.vo.WjVoteVo;
import cn.csl.wenjuan.util.JSONArrayStrToArray;
import cn.csl.wx.dao.WxUserMapper;
import cn.csl.wx.entity.WxUser;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jdk.nashorn.internal.runtime.logging.Logger;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class WjVoteServive extends BasicsService<WjVote> {
	@Resource
	private WjFootprintMapper wjFootprintMapper;
	@Resource
	private WxUserMapper wxUserMapper;
	@Resource
	private WjBannerMapper wjBannerMapper;
	@Resource
	private WjVoteMapper wjVoteMapper;
	@Resource
	private BasicsFileMapper basicsFileMapper;
	@Resource
	private WjCandidateMapper wjCandidateMapper;
	@Resource
	private WjVoterecordMapper wjVoterecordMapper;

	@Transactional
	public ResultResponse delete(String id){
		ResultResponse response = new ResultResponse();
		wjVoteMapper.delete(Long.valueOf(id));//删除投票
		wjCandidateMapper.deleteByVoteId(Long.valueOf(id));//删除候选项
		wjVoterecordMapper.deleteByVoteId(Long.valueOf(id));//删除记录
		response.setSuccessResult("删除成功");
		return response;
	}
	public List<WjVoteVo> joinVoteVo(String wxUserId){
		List<WjVoteVo> wjVoteVos = new ArrayList<>();
		List<WjVote> wjVotes = wjVoteMapper.listJoin(Long.valueOf(wxUserId));
		for (WjVote wjVote:wjVotes) {
			wjVoteVos.add(initWjVoteVo(wjVote,Long.valueOf(wxUserId),1));
		}
		return wjVoteVos;
	}
	public List<WjVoteVo> footprintVoteVo(String wxUserId){/////浏览历史的
		List<WjVoteVo> wjVoteVos = new ArrayList<>();
		List<WjVote> wjVotes = wjVoteMapper.listFootprint(Long.valueOf(wxUserId));
		for (WjVote wjVote:wjVotes) {
			wjVoteVos.add(initWjVoteVo(wjVote,Long.valueOf(wxUserId),1));
		}
		return wjVoteVos;
	}

	@Override
	public BasicsAdminMapper<WjVote> getBasicsAdminMapper(){return wjVoteMapper;}
	private List<String> initImg(String ids){
		List<String> imgs = new ArrayList<>();
		List<Long> coverIds = JSONArrayStrToArray.dateToString(ids);
		for (Long coverId:coverIds) {
			BasicsFile basicsFile = basicsFileMapper.selectByPrimaryKey(coverId);
			imgs.add(basicsFile.getFilePath());
		}
		return imgs;
	}
	private WjCandidateVo initWjCandidateVo(WjCandidate candidate){
		List<String> covers = new ArrayList<>();
		List<String> imgIntroduces = new ArrayList<>();
		if(candidate.getVoteType()!=1){ // 不为图文 要初始化介绍图片 封面
			covers = initImg(candidate.getCover());
			imgIntroduces = initImg(candidate.getImgIntroduce());
		}

		WjCandidateVo WjCandidateVo = new WjCandidateVo(candidate,covers,imgIntroduces);
		return WjCandidateVo;
	}
	/**
	 * type = 1 列表版本  vote 图片
	 * type = 2 详请页面版本  候选项
	 * @param type
	 * @return
	 */
	private WjVoteVo initWjVoteVo(WjVote vote ,Long wxUserId,int type){
		List<String> covers = initImg(vote.getCover());//封面
		List<String> imgIntroduces = initImg(vote.getImgIntroduce());//介绍
		List<String> rewarImgs = new ArrayList<>();

		String schedule="zanting";//进行中
		if(vote.getSuspend()==0){//为0 就是 暂停了
			schedule="zanting";//暂停了
		}else{// 为1
			if(new Date().before(vote.getStartTime())){//在开始前
				schedule="weikaishi";
			}else {
				if(new Date().after(vote.getEndTime())){//在结束之后
					schedule="yijieshu";
				}else{
					schedule="jinxingzhong";//进行中
				}
			}
		}

		if(vote.getReward()==1){
			rewarImgs= initImg(vote.getRewarImg());//奖励
		}

		if (type==1){// // // // // // // // // // // 列表 只需要封面
			return new WjVoteVo(vote,schedule,covers);
		}

		//取全部候选项   、。。。。。
		List<WjCandidateVo> candidateVos = new ArrayList<>();
		//该投票的评论数
		long comment = 0;
		//        可否报名
		int enlist = 1;//显示
		//useFlag = 1 已经通过报名
		List<WjCandidate>  wjCandidates  = wjCandidateMapper.listByAttrValueMap(" and useFlag = 1 and voteId = "+vote.getId(),1);
		for (WjCandidate wjCandidate:wjCandidates) {
			comment+=Long.valueOf(wjCandidate.getComment());
			candidateVos.add(initWjCandidateVo(wjCandidate));
			if(wjCandidate.getWxUserId()==wxUserId){
				enlist = 0;//我在候选人里边 不显示
			}
		}
		if(wxUserId==vote.getWxUserId()){//我是管理员
			enlist = 2;
		}else{//非管理员
			if(vote.getOutside()==1){//开启报名
				wjCandidates = wjCandidateMapper.listByAttrValueMap("and wxUserId="+wxUserId+" and voteId = "+vote.getId(),1);
				if(wjCandidates.size()>0){//看看是不是已经拒绝额
					if(wjCandidates.get(0).getUseFlag() == -1){//已经拒绝
						enlist = -1;
					}else{
						enlist = -2;//已经还在审核中
					}
				}
				if(new Date().after(vote.getStartTime())){//现在在活动开始 之后 不能 报名
					enlist = 0;
				}
			}else {
				enlist = 0;
			}
		}

		vote.setCommentNum(comment);
		// // // // // // // // // // // 全部 信息
		return new WjVoteVo(vote,schedule,covers,imgIntroduces,rewarImgs,candidateVos,enlist);
	}

	public WjVoteVo aopDetailed(String id,String wxUserId){
		WjVote vote = wjVoteMapper.selectByPrimaryKey(Long.valueOf(id));
		WjVoteVo wjVoteVo = initWjVoteVo(vote,Long.valueOf(wxUserId),3);
//加入浏览历史
		String sql = "and surface = 'wj_vote' and subscriberSurface = 'wx_user' and surfaceId="+id+" and subscriberId="+wxUserId;
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
			footprint.setSurface("wj_vote");
			footprint.setSurfaceId(Long.valueOf(id));
			wjFootprintMapper.insert(footprint);
		}
		return wjVoteVo;
	}

	@Transactional
	public ResultResponse addSave(String voteDataStr,String wxUserId) throws Exception{
		ResultResponse response =new ResultResponse();

		WxUser wxUser = wxUserMapper.selectByPrimaryKey(Long.valueOf(wxUserId));
		if(wxUser==null){
			response.setFailureResult("用户信息过期请重新登录");
			return response;
		}

		JSONObject voteData = JSONObject.parseObject(voteDataStr);
		JSONObject vote = voteData.getJSONObject("vote");
		WjVote wjVote = voteData.getObject("vote",WjVote.class);
		wjVote.setVoteCode(RandomUtil.getUUID().substring(0,6));
		//设置data
		wjVote.setVoteDataStr(voteDataStr);
		//设置开始结束时间
		wjVote.setStartTime(new Date(Long.valueOf(vote.getString("startTimeStr"))));
		wjVote.setEndTime(new Date(Long.valueOf(vote.getString("endTimeStr"))));
		///封面
		JSONArray coverIds = vote.getJSONArray("coverId");
		wjVote.setCover(coverIds.toString());
		///介绍图片
		JSONArray imgIntroduceIds = vote.getJSONArray("imgIntroduceId");
		wjVote.setImgIntroduce(imgIntroduceIds.toString());
		///奖励图片
		JSONArray rewarImgIds = vote.getJSONArray("rewarImgId");
		wjVote.setRewarImg(rewarImgIds.toString());
		//发起者
		wjVote.setWxUserId(wxUser.getId());
		//核对地区
		if(wjVote.getRestrictFlag()==0){
			wjVote.setProvince("不限");
			wjVote.setCity("不限");
			wjVote.setDistrict("不限");
		}

		//投票信息添加进去了
		wjVoteMapper.insert(wjVote);///测试先关了

		/////////更新 投票的 文件图片表信息/////////////

		///封面数量
		for (int i=0;i<coverIds.size();i++) {
			BasicsFile basicsFile = basicsFileMapper.selectByPrimaryKey(coverIds.getLong(i));
			if(basicsFile.getSurfaceId()==null){//没有主人呢
				basicsFile.setSurface("wj_vote");//表名称
				basicsFile.setNature("cover");//字段名称
				basicsFile.setSurfaceId(wjVote.getId());
				basicsFileMapper.updateByPrimaryKey(basicsFile);
			}
		}
		///介绍图片数量
		for (int i=0;i<imgIntroduceIds.size();i++) {
			BasicsFile basicsFile = basicsFileMapper.selectByPrimaryKey(imgIntroduceIds.getLong(i));
			if(basicsFile.getSurfaceId()==null){//没有主人呢
				basicsFile.setSurface("wj_vote");//表名称
				basicsFile.setNature("imgIntroduce");//字段名称
				basicsFile.setSurfaceId(wjVote.getId());
				basicsFileMapper.updateByPrimaryKey(basicsFile);
			}
		}
		///奖励图片数量
		for (int i=0;i<imgIntroduceIds.size();i++) {
			BasicsFile basicsFile = basicsFileMapper.selectByPrimaryKey(imgIntroduceIds.getLong(i));
			if(basicsFile.getSurfaceId()==null){//没有主人呢
				basicsFile.setSurface("wj_vote");//表名称
				basicsFile.setNature("rewarImg");//字段名称
				basicsFile.setSurfaceId(wjVote.getId());
				basicsFileMapper.updateByPrimaryKey(basicsFile);
			}
		}

		/////////添加候选项 ////////////
		JSONArray candidates = vote.getJSONArray("candidate");
		long c = candidates.size();
		for (Iterator iterator = candidates.iterator(); iterator.hasNext();){

			wjVote.setCandidateNum(wjVote.getCandidateNum()+1);
			wjVote.setCandidateIndex(wjVote.getCandidateIndex()+1);

			JSONObject jsonObject = (JSONObject) iterator.next();

			WjCandidate candidate= JSONObject.toJavaObject(jsonObject,WjCandidate.class);
			candidate.setOrderNum(wjVote.getCandidateIndex());
			candidate.setVoteId(wjVote.getId());
			candidate.setVoteType(wjVote.getVoteType());
			candidate.setComment("0");//默认评论数量为0

			///封面数量
			JSONArray candidateCoverIds = jsonObject.getJSONArray("coverId");
			candidate.setCover(candidateCoverIds.toString());
			///介绍图片数量
			JSONArray candidateImgIntroduceIds = vote.getJSONArray("imgIntroduceId");
			candidate.setImgIntroduce(candidateImgIntroduceIds.toString());

			candidate.setWxUserId(wxUser.getId());

			wjCandidateMapper.insert(candidate);

			///封面数量
			for (int i=0;i<candidateCoverIds.size();i++) {
				BasicsFile basicsFile = basicsFileMapper.selectByPrimaryKey(candidateCoverIds.getLong(i));
				if(basicsFile.getSurfaceId()==null) {//没有主人呢
					basicsFile.setSurface("wj_candidate");//表名称
					basicsFile.setNature("cover");//字段名称
					basicsFile.setSurfaceId(candidate.getId());
					basicsFileMapper.updateByPrimaryKey(basicsFile);
				}
			}
			///介绍图片数量
			for (int i=0;i<candidateImgIntroduceIds.size();i++) {
				BasicsFile basicsFile = basicsFileMapper.selectByPrimaryKey(candidateImgIntroduceIds.getLong(i));
				if(basicsFile.getSurfaceId()==null) {//没有主人呢
					basicsFile.setSurface("wj_candidate");//表名称
					basicsFile.setNature("imgIntroduce");//字段名称
					basicsFile.setSurfaceId(candidate.getId());
					basicsFileMapper.updateByPrimaryKey(basicsFile);
				}
			}
		}

		wjVoteMapper.updateByPrimaryKey(wjVote);//双向 再次更新
		response.setSuccessResult(wjVote);
		return response;
	}


	public List<WjVoteVo> listVoteVosByWxUserId(String wxUserId){
		List<WjVoteVo> wjVoteVos = new ArrayList<>();
		List<WjVote> wjVotes = wjVoteMapper.listByAttribute("wxUserId",wxUserId,1);
		for (WjVote wjVote:wjVotes) {
			wjVoteVos.add(initWjVoteVo(wjVote,Long.valueOf(wxUserId),1));
		}
		return wjVoteVos;
	}
	@Override
	public ResultResponse editSave(WjVote obj){
		ResultResponse resultResponse;
		resultResponse = checkInfo(obj);
		if (resultResponse.isError()) {
			return resultResponse;
		}
		WjVote dbObj = getBasicsAdminMapper().selectByPrimaryKey(obj.getId());
		getBasicsAdminMapper().updateByPrimaryKey(dbObj);
		resultResponse.setSuccessResult("修改成功");
		return resultResponse;
	}

	@Transactional
	public ResultResponse addCandidate(WjCandidate wjCandidate){
		ResultResponse resultResponse = new ResultResponse();
		WjVote vote = wjVoteMapper.selectByPrimaryKey(wjCandidate.getVoteId());
		if(wjCandidate.getUseFlag()==1){
			vote.setCandidateNum(vote.getCandidateNum()+1);
			vote.setCandidateIndex(vote.getCandidateIndex()+1);
			wjVoteMapper.updateByPrimaryKey(vote);
		}
		wjCandidate.setCover(JSONObject.toJSONString(wjCandidate.getCoverId()));
		wjCandidate.setImgIntroduce(JSONObject.toJSONString(wjCandidate.getImgIntroduceId()));
		if(wjCandidate.getWxUserId()==vote.getWxUserId()){// 管理员本人 添加
			wjCandidate.setUseFlag(1);
		}
		wjCandidate.setComment("0");
		wjCandidateMapper.insert(wjCandidate);
		if(wjCandidate.getVoteType()!=1){//1为文字类型
			//封面 cover
			List<Long> coverIds = wjCandidate.getCoverId();
			for (int i=0;i<coverIds.size();i++) {
				BasicsFile basicsFile = basicsFileMapper.selectByPrimaryKey(coverIds.get(i));
				if(basicsFile.getSurfaceId()==null){//没有主人呢
					basicsFile.setNature("cover");//字段名称
					basicsFile.setSurface("wj_candidate");//表名称
					basicsFile.setSurfaceId(wjCandidate.getId());
					basicsFileMapper.updateByPrimaryKey(basicsFile);
				}
			}
			///介绍图片
			List<Long> imgIntroduceIds = wjCandidate.getImgIntroduceId();
			for (int i=0;i<imgIntroduceIds.size();i++) {
				BasicsFile basicsFile = basicsFileMapper.selectByPrimaryKey(imgIntroduceIds.get(i));
				if(basicsFile.getSurfaceId()==null){//没有主人呢
					basicsFile.setNature("imgIntroduce");//字段名称
					basicsFile.setSurface("wj_candidate");//表名称
					basicsFile.setSurfaceId(wjCandidate.getId());
					basicsFileMapper.updateByPrimaryKey(basicsFile);
				}
			}
		}

		resultResponse.setSuccessResult("报名成功");
		return resultResponse;
	}


	public ResultResponse listCandidate(Long voteId){
		ResultResponse response = new ResultResponse();
		List<WjCandidate> candidates = wjCandidateMapper.listByAttribute("voteId",String.valueOf(voteId),1);
		List<WjCandidateVo> candidateVos = new ArrayList<>();
		for (WjCandidate wjCandidate:candidates) {
			candidateVos.add(initWjCandidateVo(wjCandidate));
		}
		response.setSuccessResult(candidateVos);
		//-1 拒绝 0 待审核 1通过
		return response;
	}

	public ResultResponse editCandidate(int useFlag,Long id){
		ResultResponse response = new ResultResponse();
		WjCandidate candidate = wjCandidateMapper.selectByPrimaryKey(id);
		candidate.setUseFlag(useFlag);
		//-1 拒绝 0 待审核 1通过
		wjCandidateMapper.updateByPrimaryKey(candidate);
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
		List<WjVote> votes = this.getBasicsAdminMapper().listPageObjs(tableUploadUtil);
		List<WjVoteVo> voteVos = new ArrayList<>();
		for (WjVote vote:votes){
			voteVos.add(initWjVoteVo(vote,vote.getWxUserId(),1));
		}
		Map<String,Object> map = new HashMap<>();
		map.put("voteVos",voteVos);
		if(banner==1){
			List<WjBanner> banners = wjBannerMapper.listBanners("vote");
			map.put("banners",banners);
		}

		tableReturnUtil.setData(map);
		return tableReturnUtil;
	}
}
