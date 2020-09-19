package cn.csl.wenjuan.front.controller;

import cn.csl.basics.model.ResultResponse;
import cn.csl.basics.util.*;
import cn.csl.manage.redis.JedisUtil;
import cn.csl.manage.redis.utils.Keys;
import cn.csl.manage.redis.utils.Strings;
import cn.csl.wenjuan.entity.WjBanner;
import cn.csl.wenjuan.entity.WjCandidate;
import cn.csl.wenjuan.entity.WjQuestion;
import cn.csl.wenjuan.front.dto.PrescribedDto;
import cn.csl.wenjuan.front.vo.WjQuestionVo;
import cn.csl.wenjuan.front.vo.WjVoteVo;
import cn.csl.wenjuan.service.WjBannerServive;
import cn.csl.wenjuan.service.WjQuestionServive;
import cn.csl.wenjuan.service.WjVoteServive;
import cn.csl.wx.util.GetminiqrQrUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.net.util.IPAddressUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping({"/wenjuan/front/question"})
public class FrontWjQuestionController {
    @Resource
    private HttpSession httpSession;
    @Resource
    private WjQuestionServive wjQuestionServive;
    @Resource
    private WjBannerServive wjBannerServive;

    public WjQuestionServive getBasicsService() {
        return wjQuestionServive;
    }
    @Autowired
    private JedisUtil jedisUtil;

    @RequestMapping({"/piliang"})
    @ResponseBody
    public ResultResponse piliang(String examineFlag,String filename,HttpServletRequest request){
        ResultResponse response = getBasicsService().piliang(examineFlag,filename,request);
        return response;
    }

    @RequestMapping({"/zipFile"})
    @ResponseBody
    public ResultResponse zipFile(String questionId,HttpServletRequest request){
        ResultResponse response = getBasicsService().changeZipFile(questionId,request);
        return response;
    }

    @RequestMapping({"/dafen"})
    @ResponseBody
    public ResultResponse dafen(String dafen,String problemId,HttpServletRequest request){
        ResultResponse response = new ResultResponse();
        try{
            response = getBasicsService().editObtain(dafen,problemId,request);
        }catch (Exception e){
            e.printStackTrace();
            response.setFailureResult("提交失败");
        }
        return response;
    }

    @RequestMapping({"/reply"})
    @ResponseBody
    public ResultResponse reply(PrescribedDto prescribedDto, String wentisStr, String questionId,HttpServletRequest request){
        ResultResponse response = new ResultResponse();
        try{
            prescribedDto.setIpAddress(AddressUtil.getIpAddress(request));
            response = wjQuestionServive.editReply(wentisStr,questionId,prescribedDto);
        }catch (Exception e){
            e.printStackTrace();
            response.setFailureResult("提交失败");
        }
        return response;
    }
    @RequestMapping({"/loadDetailed"})
    @ResponseBody
    public ResultResponse loadDetailed(String id,String wxUserId){
        ResultResponse response = new ResultResponse();
        try{
            response = wjQuestionServive.aopQuestionVo(id,wxUserId);
        }catch (Exception e){
            e.printStackTrace();
            response.setFailureResult("查看失败");
        }
        return response;
    }
    @RequestMapping({"/addSave"})
    @ResponseBody
    public ResultResponse addSave(String settingStr,String wentisStr,String wxUserId,HttpServletRequest request){
        ResultResponse response = new ResultResponse();
        try{
            response = this.getBasicsService().addSave(settingStr,wentisStr,wxUserId,request);
        }catch (Exception e){
            e.printStackTrace();
            response.setFailureResult("添加失败");
        }
        return response;
    }
    @RequestMapping({"/editSave"})
    @ResponseBody
    public ResultResponse editSave(String settingStr,HttpServletRequest request){
        ResultResponse response = new ResultResponse();
        try{
            JSONObject settingData = JSONObject.parseObject(settingStr);
            WjQuestion question = this.getBasicsService().selectByPrimaryKey(settingData.getLong("id"));
            question.setStartTime(DatetimeUtils.stringToDate(DatetimeUtils.stampToDate(settingData.getString("startTimeStr")),"yyyy-MM-dd HH:mm:ss"));
            question.setEndTime(DatetimeUtils.stringToDate(DatetimeUtils.stampToDate(settingData.getString("endTimeStr")),"yyyy-MM-dd HH:mm:ss"));
            question.setName(settingData.getString("name"));
            question.setIntroduce(settingData.getString("introduce"));
            question.setQuestionDataStr(settingStr);
            question.setOvert(settingData.getInteger("overt"));
            response = this.getBasicsService().editSave2(question,request);
        }catch (Exception e){
            e.printStackTrace();
            response.setFailureResult("修改失败");
        }
        return response;
    }
    @RequestMapping({"/changeSuspend"})
    @ResponseBody
    public ResultResponse changeSuspend(String id){////////暂停或开启
        ResultResponse response = new ResultResponse();
        try{
            response = this.getBasicsService().changeSuspend(Long.valueOf(id));
        }catch (Exception e){
            e.printStackTrace();
            response.setFailureResult("修改失败");
        }
        return response;
    }
    @RequestMapping({"/list"})
    @ResponseBody
    public ResultResponse list(String wxUserId){
        ResultResponse response = new ResultResponse();
        try{
            response.setSuccessResult(getBasicsService().listQuestionVoByWxUserId(wxUserId));
        }catch (Exception e){
            e.printStackTrace();
            response.setFailureResult("列表失败");
        }
        return response;
    }
    @RequestMapping({"/delete"})
    @ResponseBody
    public ResultResponse delete(String id){
        ResultResponse response = new ResultResponse();
        try{
            response = this.getBasicsService().delete(id);
        }catch (Exception e){
            e.printStackTrace();
            response.setFailureResult("删除失败");
        }
        return response;
    }
    @RequestMapping({"/analysis"})
    @ResponseBody
    public ResultResponse analysis(String id){
        ResultResponse response = new ResultResponse();
        try{
            response = this.getBasicsService().analysis(id);
        }catch (Exception e){
            e.printStackTrace();
            response.setFailureResult("分析数据查询失败");
        }
        return response;
    }
    @RequestMapping({"/listAnswerVo"})
    @ResponseBody
    public ResultResponse listAnswerVoByProblemId(String problemId){
        ResultResponse response = new ResultResponse();
        try{
            response = this.getBasicsService().listAnswerVoByProblemId(problemId);
        }catch (Exception e){
            e.printStackTrace();
            response.setFailureResult("分析数据查询失败");
        }
        return response;
    }

    @RequestMapping({"/listJson"})
    @ResponseBody
    public ResultResponse listJson(TableUploadUtil tableUploadUtil, int banner){
        ResultResponse response = new ResultResponse();
        TableReturnUtil tableReturnUtil = new TableReturnUtil();
        try{
            tableUploadUtil.setStart((tableUploadUtil.getDraw()-1)*tableUploadUtil.getLength());
            tableReturnUtil = this.getBasicsService().listJson2(tableUploadUtil,banner);
            response.setSuccessResult(tableReturnUtil);
        }catch (Exception e){
            e.printStackTrace();
            response.setFailureResult("失败");
        }
        return response;
    }

    @RequestMapping({"/join"})
    @ResponseBody
    public ResultResponse join(String wxUserId){
        ResultResponse response = new ResultResponse();
        try{
            response.setSuccessResult(getBasicsService().joinQuestionVo(wxUserId));
        }catch (Exception e){
            e.printStackTrace();
            response.setFailureResult("列表失败");
        }
        return response;
    }
}
