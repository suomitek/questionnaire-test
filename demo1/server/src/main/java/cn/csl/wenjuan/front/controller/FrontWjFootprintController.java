package cn.csl.wenjuan.front.controller;

import cn.csl.basics.model.ResultResponse;
import cn.csl.wenjuan.entity.WjQuestion;
import cn.csl.wenjuan.service.WjCommentrecordServive;
import cn.csl.wenjuan.service.WjQuestionServive;
import cn.csl.wenjuan.service.WjVoteServive;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping({"/wenjuan/front/footprint"})
public class FrontWjFootprintController {
    @Resource
    private WjVoteServive wjVoteServive;
    @Resource
    private WjQuestionServive wjQuestionServive;



    @RequestMapping({"/list"})
    @ResponseBody
    public ResultResponse list(String subscriberId){
        ResultResponse response = new ResultResponse();
        try{
            Map<String,Object> map = new HashMap<>();
            map.put("wjVoteVos",wjVoteServive.footprintVoteVo(subscriberId));
            map.put("wjQuestionVos",wjQuestionServive.footprintQuestionVo(subscriberId));
            response.setSuccessResult(map);
        }catch (Exception e){
            e.printStackTrace();
            response.setFailureResult("查询失败");
        }
        return response;
    }
}
