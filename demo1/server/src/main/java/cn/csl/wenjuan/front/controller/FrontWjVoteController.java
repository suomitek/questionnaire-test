package cn.csl.wenjuan.front.controller;

import cn.csl.basics.model.ResultResponse;
import cn.csl.basics.util.TableReturnUtil;
import cn.csl.basics.util.TableUploadUtil;
import cn.csl.manage.entity.SysApi;
import cn.csl.manage.service.SysApiServive;
import cn.csl.wenjuan.entity.WjCandidate;
import cn.csl.wenjuan.entity.WjVote;
import cn.csl.wenjuan.front.vo.WjVoteVo;
import cn.csl.wenjuan.service.WjVoteServive;
import cn.csl.wx.util.GetminiqrQrUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.List;

@Controller
@RequestMapping({"/wenjuan/front/vote"})
public class FrontWjVoteController {
//    @Resource
//    private HttpServletRequest httpServletRequest;
    @Resource
    private HttpSession httpSession;
    @Resource
    private WjVoteServive wjVoteServive;

    public WjVoteServive getBasicsService() {
        return wjVoteServive;
    }


    @RequestMapping({"/join"})
    @ResponseBody
    public ResultResponse join(String wxUserId){
        ResultResponse response = new ResultResponse();
        try{
            response.setSuccessResult(getBasicsService().joinVoteVo(wxUserId));
        }catch (Exception e){
            e.printStackTrace();
            response.setFailureResult("列表失败");
        }
        return response;
    }

    @RequestMapping({"/listJson"})
    @ResponseBody
    public ResultResponse listJson(TableUploadUtil tableUploadUtil, int banner){
        ResultResponse response = new ResultResponse();
        TableReturnUtil tableReturnUtil;
        try{
            tableUploadUtil.setStart((tableUploadUtil.getDraw()-1)*tableUploadUtil.getLength());
            tableReturnUtil = wjVoteServive.listJson2(tableUploadUtil,banner);
            response.setSuccessResult(tableReturnUtil);
        }catch (Exception e){
            e.printStackTrace();
            response.setFailureResult("失败");
        }
        return response;
    }
    @RequestMapping({"/listCandidate"})
    @ResponseBody
    public ResultResponse listCandidate(Long voteId){
        ResultResponse response = new ResultResponse();
        try{
            response = wjVoteServive.listCandidate(voteId);
        }catch (Exception e){
            e.printStackTrace();
            response.setFailureResult("查看失败");
        }
        return response;
    }

    @RequestMapping({"/editCandidate"})
    @ResponseBody
    public ResultResponse editCandidate(int useFlag,Long id){
        ResultResponse response = new ResultResponse();
        try{
            response = wjVoteServive.editCandidate(useFlag,id);
        }catch (Exception e){
            e.printStackTrace();
            response.setFailureResult("查看失败");
        }
        return response;
    }

    @RequestMapping({"/addCandidate"})
    @ResponseBody
    public ResultResponse addCandidate(WjCandidate wjCandidate){
        ResultResponse response = new ResultResponse();
        try{
            response.setSuccessResult(this.getBasicsService().addCandidate(wjCandidate));
        }catch (Exception e){
            e.printStackTrace();
            response.setFailureResult("查看失败");
        }
        return response;
    }




    @RequestMapping({"/loadDetailed"})
    @ResponseBody
    public ResultResponse loadDetailed(String id,String wxUserId){
        ResultResponse response = new ResultResponse();
        try{
            response.setSuccessResult(this.getBasicsService().aopDetailed(id,wxUserId));
        }catch (Exception e){
            e.printStackTrace();
            response.setFailureResult("查看失败");
        }
        return response;
    }
    @RequestMapping({"/addSave"})
    @ResponseBody
    public ResultResponse addSave(String voteDataStr,String wxUserId){
        ResultResponse response = new ResultResponse();
        try{
            response = this.getBasicsService().addSave(voteDataStr,wxUserId);
        }catch (Exception e){
            e.printStackTrace();
            response.setFailureResult("添加失败");
        }
        return response;
    }

    @RequestMapping({"/list"})
    @ResponseBody
    public ResultResponse list(String wxUserId){
        ResultResponse response = new ResultResponse();
        try{
            List<WjVoteVo> wjVoteVos = this.getBasicsService().listVoteVosByWxUserId(wxUserId);
            response.setSuccessResult(wjVoteVos);
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
//            response.setSuccessResult("成功");
            response = this.getBasicsService().delete(id);
        }catch (Exception e){
            e.printStackTrace();
            response.setFailureResult("删除失败");
        }
        return response;
    }
}
