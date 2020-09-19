package cn.csl.manage.admin.controller;

import cn.csl.basics.model.ResultResponse;
import cn.csl.basics.util.TableReturnUtil;
import cn.csl.basics.util.TableUploadUtil;
import cn.csl.basics.util.annotation.search.LoadSearchAnnotationAttribute;
import cn.csl.manage.service.SysSubscriderServive;
import cn.csl.manage.service.SysUrlServive;
import cn.csl.manage.entity.SysFrontlink;
import cn.csl.manage.entity.SysSubscriber;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping({"/manage/admin/subscrider"})
public class AdminSysSubscriderController {
    @Resource
    private HttpSession httpSession;
    @Resource
    private SysSubscriderServive sysSubscriderServive;

    public SysSubscriderServive getBasicsService() {
        return sysSubscriderServive;
    }

    private String getBasicsUrl() {
        return "manage/admin/subscrider";
    }


    @RequestMapping({"/distribution"})
    public String distribution(Model model, String userId)
    {
        model.addAttribute("url", getBasicsUrl());
        model.addAttribute("roleList", this.sysSubscriderServive.distribution(userId));
        model.addAttribute("userId", userId);
        return getBasicsUrl()+"/distribution";
    }
    @RequestMapping({"/distributionSave"})
    @ResponseBody
    public ResultResponse distributionSave(Model model, String userId,String roles)
    {
        ResultResponse response = new ResultResponse();
        try{

            response=sysSubscriderServive.changeUserRole(userId,roles);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            response.setFailureResult("不可用");
        }
        return response;
    }

    @RequestMapping({"/changeUseFlag"})
    @ResponseBody
    public ResultResponse changeUseFlag(String id){
        ResultResponse response = new ResultResponse();
        try{
            response = this.getBasicsService().changeUseFlag(id);
        }catch (Exception e){
            e.printStackTrace();
            response.setFailureResult("删除失败");
        }
        return response;
    }
    @RequestMapping({"/edit"})
    public String edit(Model model, String id){
        model.addAttribute("url", getBasicsUrl());
        Object obj = this.getBasicsService().selectByPrimaryKey(id);
        model.addAttribute("obj", obj);
        return getBasicsUrl()+"/edit";
    }

    @RequestMapping({"/list"})
    public String list(Model model){
        model.addAttribute("map", LoadSearchAnnotationAttribute.getObjectValue(SysSubscriber.class));
        model.addAttribute("url", getBasicsUrl());
        return getBasicsUrl()+"/list";
    }

    @RequestMapping({"/listJson"})
    @ResponseBody
    public TableReturnUtil listJson(TableUploadUtil tableUploadUtil){
        TableReturnUtil tableReturnUtil = new TableReturnUtil();
        try{
            tableReturnUtil = this.getBasicsService().listJson(tableUploadUtil);
        }catch (Exception e){
            e.printStackTrace();
        }
        return tableReturnUtil;
    }
}
