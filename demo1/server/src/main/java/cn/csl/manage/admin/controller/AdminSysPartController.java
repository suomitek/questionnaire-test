package cn.csl.manage.admin.controller;

import cn.csl.basics.model.ResultResponse;
import cn.csl.basics.util.TableReturnUtil;
import cn.csl.basics.util.TableUploadUtil;
import cn.csl.basics.util.annotation.search.LoadSearchAnnotationAttribute;
import cn.csl.manage.service.SysPartServive;
import cn.csl.manage.service.SysUrlServive;
import cn.csl.manage.entity.SysFrontrole;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping({"/manage/admin/part"})
public class AdminSysPartController {
    @Resource
    private HttpSession httpSession;
    @Resource
    private SysPartServive sysPartServive;
    @Resource
    private SysUrlServive sysUrlServive;

    public SysPartServive getBasicsService() {
        return sysPartServive;
    }

    private String getBasicsUrl() {
        return "manage/admin/part";
    }


    @RequestMapping({"/distribution"})
    public String distribution(Model model,String id){
        model.addAttribute("map", LoadSearchAnnotationAttribute.getObjectValue(SysFrontrole.class));
        model.addAttribute("url", getBasicsUrl());
        model.addAttribute("roleId",id);
        model.addAttribute("sysFrontlinkVos",sysPartServive.listAllUrls(id));
        return getBasicsUrl()+"/distribution";
    }
    @RequestMapping({"/distributionSave"})
    @ResponseBody
    public ResultResponse distributionSave(String roleId,String linkIds){
        ResultResponse response = new ResultResponse();
        try{
            response = this.sysPartServive.changeUserRole(roleId,linkIds);
        }
        catch (Exception e){
            e.printStackTrace();
            response.setFailureResult("添加失败");
        }
        return response;
    }

    @RequestMapping({"/add"})
    public String add(Model model){
        model.addAttribute("url", getBasicsUrl());
        return getBasicsUrl()+"/add";
    }

    @RequestMapping({"/addSave"})
    @ResponseBody
    public ResultResponse addSave(SysFrontrole sysFrontrole){
        ResultResponse response = new ResultResponse();
        try{
            response = this.getBasicsService().addSave(sysFrontrole);
        }
        catch (Exception e){
            e.printStackTrace();
            response.setFailureResult("添加失败");
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
    @RequestMapping({"/edit"})
    public String edit(Model model, String id){
        model.addAttribute("url", getBasicsUrl());
        Object obj = this.getBasicsService().selectByPrimaryKey(id);
        model.addAttribute("obj", obj);
        return getBasicsUrl()+"/edit";
    }

    @RequestMapping({"/editSave"})
    @ResponseBody
    public ResultResponse editSave(SysFrontrole sysFrontrole){
        ResultResponse response = new ResultResponse();
        try{
            response = this.getBasicsService().editSave(sysFrontrole);
        }catch (Exception e){
            e.printStackTrace();
            response.setFailureResult("修改失败");
        }
        return response;
    }

    @RequestMapping({"/loadCheckAttributeIsExistence"})
    @ResponseBody
    public ResultResponse loadCheckAttributeIsExistence(String attribute,String checkVal,Long id){
        ResultResponse response = new ResultResponse();
        try{
            response = this.getBasicsService().loadCheckAttributeIsExistence(attribute,checkVal,id);
        }catch (Exception e){
            e.printStackTrace();
            response.setFailureResult("不可用");
        }
        return response;
    }
    @RequestMapping({"/list"})
    public String list(Model model){
        model.addAttribute("map", LoadSearchAnnotationAttribute.getObjectValue(SysFrontrole.class));
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
