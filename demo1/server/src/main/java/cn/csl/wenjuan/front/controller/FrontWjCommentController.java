package cn.csl.wenjuan.front.controller;

import cn.csl.basics.model.ResultResponse;
import cn.csl.basics.util.TableReturnUtil;
import cn.csl.basics.util.TableUploadUtil;
import cn.csl.wenjuan.entity.WjComment;
import cn.csl.wenjuan.entity.WjVote;
import cn.csl.wenjuan.service.WjCommentServive;
import cn.csl.wenjuan.service.WjVoteServive;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping({"/wenjuan/front/comment"})
public class FrontWjCommentController {
    @Resource
    private WjCommentServive wjCommentServive;

    public WjCommentServive getBasicsService() {
        return wjCommentServive;
    }

    @RequestMapping({"/addSave"})
    @ResponseBody
    public ResultResponse addSave(WjComment wjComment){
        ResultResponse response = new ResultResponse();
        try{
            response = this.getBasicsService().addSave(wjComment);
        }catch (Exception e){
            e.printStackTrace();
            response.setFailureResult("添加失败");
        }
        return response;
    }

    @RequestMapping({"/listJson"})
    @ResponseBody
    public ResultResponse listJson(TableUploadUtil tableUploadUtil,String surface,String nature,
                                   String subscriberId,String subscriberSurface){
        ResultResponse response = new ResultResponse();
        TableReturnUtil tableReturnUtil = new TableReturnUtil();
        try{
            tableUploadUtil.setStart((tableUploadUtil.getDraw()-1)*tableUploadUtil.getLength());
            if(StringUtils.isBlank(tableUploadUtil.getSql())){
                tableUploadUtil.setSql(" and surface='"+surface+"' and nature='"+nature+"'");
                tableUploadUtil.setSql(tableUploadUtil.getSql()+ " and subscriberId="+subscriberId
                        +" and subscriberSurface='"+subscriberSurface+"'");
            }else{
                tableUploadUtil.setSql(tableUploadUtil.getSql()+" and surface='"+surface+"' and nature='"+nature+"'");
                tableUploadUtil.setSql(tableUploadUtil.getSql()+ " and subscriberId="+subscriberId
                        +" and subscriberSurface='"+subscriberSurface+"'");
            }

            tableReturnUtil = this.getBasicsService().listJson2(tableUploadUtil,subscriberId,subscriberSurface);
            response.setSuccessResult(tableReturnUtil);
        }catch (Exception e){
            e.printStackTrace();
            response.setFailureResult("失败");
        }
        return response;
    }
}
