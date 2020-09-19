package cn.csl.manage.admin.controller;

import cn.csl.basics.model.ResultResponse;
import cn.csl.basics.util.TableReturnUtil;
import cn.csl.basics.util.TableUploadUtil;
import cn.csl.basics.util.annotation.search.LoadSearchAnnotationAttribute;
import cn.csl.manage.service.SysApiServive;
import cn.csl.manage.service.SysUrlServive;
import cn.csl.manage.entity.SysApi;
import cn.csl.manage.entity.SysFrontlink;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping({"/manage/admin/api"})
public class AdminSysApiController {
    @Resource
    private HttpSession httpSession;
    @Resource
    private SysApiServive sysApiServive;

    public SysApiServive getBasicsService() {
        return sysApiServive;
    }

    private String getBasicsUrl() {
        return "manage/admin/api";
    }


    @RequestMapping({"/addSave"})
    @ResponseBody
    public ResultResponse addSave(SysApi sysApi){
        ResultResponse response = new ResultResponse();
        try{
            response = this.getBasicsService().addSave(sysApi);
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

    @RequestMapping({"/editSave"})
    @ResponseBody
    public ResultResponse editSave(SysApi sysApi){
        ResultResponse response = new ResultResponse();
        try{
            response = this.getBasicsService().editSave(sysApi);
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
    public String list(Model model,String resourceId){
        model.addAttribute("url", getBasicsUrl());
        model.addAttribute("resourceId", resourceId);
        model.addAttribute("apis", sysApiServive.listByAttribute("resourceId",resourceId));
        return getBasicsUrl()+"/list";
    }
}
