package cn.csl.wenjuan.front.controller;

import cn.csl.basics.model.ResultResponse;
import cn.csl.basics.util.TableReturnUtil;
import cn.csl.basics.util.TableUploadUtil;
import cn.csl.wenjuan.entity.WjComment;
import cn.csl.wenjuan.entity.WjCommentrecord;
import cn.csl.wenjuan.service.WjCommentServive;
import cn.csl.wenjuan.service.WjCommentrecordServive;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping({"/wenjuan/front/commentrecord"})
public class FrontWjCommentrecordController {
    @Resource
    private WjCommentrecordServive wjCommentrecordServive;

    public WjCommentrecordServive getBasicsService() {
        return wjCommentrecordServive;
    }

    @RequestMapping({"/addSave"})
    @ResponseBody
    public ResultResponse addSave(WjCommentrecord wjCommentrecord){
        ResultResponse response = new ResultResponse();
        try{
            response = this.getBasicsService().addSave(wjCommentrecord);
        }catch (Exception e){
            e.printStackTrace();
            response.setFailureResult("点赞失败");
        }
        return response;
    }
}
