package cn.csl.wx.admin.controller;

import cn.csl.basics.model.ResultResponse;
import cn.csl.wx.entity.WxParam;
import cn.csl.wx.service.WxParamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/wx/admin/param")
public class WxParamController {
    @Resource
    private WxParamService wxParamService;

    public WxParamService getBasicsService() {
        return wxParamService;
    }

    private String getBasicsUrl() {
        return "wx/admin/param";
    }

    @RequestMapping({"/edit"})
    public String edit(Model model, String id){
        model.addAttribute("url", getBasicsUrl());
        WxParam wxParam = this.getBasicsService().selectByPrimaryKey(id);
        model.addAttribute("wxParam", wxParam);
        return getBasicsUrl()+"/edit";
    }

    @RequestMapping({"/editSave"})
    @ResponseBody
    public ResultResponse editSave(WxParam wxParam){
        ResultResponse response = new ResultResponse();
        try{
            response = this.getBasicsService().editSave(wxParam);
        }catch (Exception e){
            e.printStackTrace();
            response.setFailureResult("修改失败");
        }
        return response;
    }
}
