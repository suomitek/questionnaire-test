package cn.csl.wenjuan.admin.controller;

import cn.csl.basics.model.ResultResponse;
import cn.csl.basics.util.TableReturnUtil;
import cn.csl.basics.util.TableUploadUtil;
import cn.csl.basics.util.annotation.search.LoadSearchAnnotationAttribute;
import cn.csl.wenjuan.service.WjBannerServive;
import cn.csl.wenjuan.entity.WjBanner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping({"/wenjuan/admin/banner"})
public class AdminWjBannerController {
    @Resource
    private HttpSession httpSession;
    @Resource
    private WjBannerServive jzBannerServive;

    public WjBannerServive getBasicsService() {
        return jzBannerServive;
    }

    private String getBasicsUrl() {
        return "wenjuan/admin/banner";
    }

    @RequestMapping({"/add"})
    public String add(Model model){
        model.addAttribute("url", getBasicsUrl());
        return getBasicsUrl()+"/add";
    }

    @RequestMapping({"/addSave"})
    @ResponseBody
    public ResultResponse addSave(WjBanner bdBanner){
        ResultResponse response = new ResultResponse();
        try{
            response = this.getBasicsService().addSave(bdBanner);
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
        WjBanner bdBanner = this.getBasicsService().selectByPrimaryKey(id);
        model.addAttribute("obj", bdBanner);
        return getBasicsUrl()+"/edit";
    }

    @RequestMapping({"/editSave"})
    @ResponseBody
    public ResultResponse editSave(WjBanner bdBanner){
        ResultResponse response = new ResultResponse();
        try{
            response = this.getBasicsService().editSave(bdBanner);
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
        model.addAttribute("map", LoadSearchAnnotationAttribute.getObjectValue(WjBanner.class));
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
