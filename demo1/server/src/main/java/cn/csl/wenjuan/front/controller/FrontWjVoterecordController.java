package cn.csl.wenjuan.front.controller;

import cn.csl.basics.model.ResultResponse;
import cn.csl.basics.util.AddressUtil;
import cn.csl.wenjuan.front.dto.PrescribedDto;
import cn.csl.wenjuan.service.WjVoterecordServive;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping({"/wenjuan/front/voterecord"})
public class FrontWjVoterecordController {
//    @Resource
//    private HttpServletRequest httpServletRequest;
    @Resource
    private HttpSession httpSession;
    @Resource
    private WjVoterecordServive wjVoterecordServive;

    public WjVoterecordServive getBasicsService() {
        return wjVoterecordServive;
    }

    @RequestMapping({"/addSave"})
    @ResponseBody
    public ResultResponse addSave(PrescribedDto prescribedDto, String candidateId, HttpServletRequest request){
        ResultResponse response = new ResultResponse();
        try{
            prescribedDto.setIpAddress(AddressUtil.getIpAddress(request));
            response = this.getBasicsService().addSave(prescribedDto,candidateId);
        }catch (Exception e){
            e.printStackTrace();
            response.setFailureResult("添加失败");
        }
        return response;
    }
//
//    @RequestMapping({"/list"})
//    @ResponseBody
//    public ResultResponse list(String wxUserId){
//        ResultResponse response = new ResultResponse();
//        try{
//            List<WjVote> wjVotes = this.getBasicsService().listByAttribute("wxUserId",wxUserId);
//            response.setSuccessResult(wjVotes);
//        }catch (Exception e){
//            e.printStackTrace();
//            response.setFailureResult("列表失败");
//        }
//        return response;
//    }
}
