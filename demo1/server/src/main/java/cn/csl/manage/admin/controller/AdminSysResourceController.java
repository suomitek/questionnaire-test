package cn.csl.manage.admin.controller;

import cn.csl.manage.entity.SysResource;
import cn.csl.basics.model.ResultResponse;
import cn.csl.manage.service.SysResourceServive;
import cn.csl.basics.util.TableReturnUtil;
import cn.csl.basics.util.TableUploadUtil;
import cn.csl.basics.util.annotation.search.LoadSearchAnnotationAttribute;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping({"/manage/admin/resource"})
public class AdminSysResourceController {
    @Resource
    private SysResourceServive sysResourceServive;
    @RequestMapping({"/add"})
    public String add(Model model,String parentId){
        SysResource sysResource;
        if("0".equals(parentId)){
            sysResource = new SysResource();
            sysResource.setResType(0);
            sysResource.setId(0L);
            sysResource.setResName("无父菜单");
        }else{
            sysResource=sysResourceServive.selectByPrimaryKey(parentId);
        }

        model.addAttribute("pResource",sysResource);
        return "manage/admin/resource/add";
    }
    @RequestMapping({"/addSave"})
    @ResponseBody
    public ResultResponse addSave(SysResource sysResource,String apis){
        ResultResponse response = new ResultResponse();
        try
        {
            if (StringUtils.isBlank(sysResource.getResUrl())&&sysResource.getResType()==1){
                sysResource.setResUrl("/"+sysResource.getResCode());
            }
            response = this.sysResourceServive.addSave(sysResource,apis);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            response.setFailureResult("添加失败");
        }
        return response;
    }
    @RequestMapping({"/edit"})
    public String edit(Model model, String id){
        SysResource sysResource = this.sysResourceServive.selectByPrimaryKey(id);
        model.addAttribute("sysResource", sysResource);
        model.addAttribute("pResource", this.sysResourceServive.selectByPrimaryKey(sysResource.getParentId().toString()));
        return "manage/admin/resource/edit";
    }

    @RequestMapping({"/editSave"})
    @ResponseBody
    public ResultResponse editSave(SysResource sysResource,String apis){
        ResultResponse response = new ResultResponse();
        try
        {
            response = this.sysResourceServive.changeSave(sysResource,apis);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            response.setFailureResult("修改失败");
        }
        return response;
    }

    @RequestMapping({"/delete"})
    @ResponseBody
    public ResultResponse delete(String id){
        ResultResponse response = new ResultResponse();
        try
        {
            response = this.sysResourceServive.delete(id);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            response.setFailureResult("删除失败");
        }
        return response;
    }

    @RequestMapping({"/listRoleZtree"})
    public String listRoleZtree(Model model,String sysRoleId)
    {
        model.addAttribute("sysRoleId",sysRoleId);
        return "manage/admin/resource/listRoleZtree";
    }

    @RequestMapping({"/listRoleZtreeJson"})
    @ResponseBody
    public ResultResponse listRoleZtreeJson(String sysRoleId)
    {
        return sysResourceServive.listRoleZtreeJson(sysRoleId);
    }
    @RequestMapping({"/editRoleResource"})
    @ResponseBody
    public ResultResponse editRoleResource(String sysRoleId,String  ids)
    {
        return sysResourceServive.editRoleResource(sysRoleId,ids);
    }

    @RequestMapping({"/listAllZtree"})
    public String listAllZtree()
    {
        return "manage/admin/resource/listAllZtree";
    }

    @RequestMapping({"/list"})
    public String list(Model model)
    {
        model.addAttribute("map", LoadSearchAnnotationAttribute.getObjectValue(SysResource.class));
        return "manage/admin/resource/list";
    }
    @RequestMapping({"/listJson"})
    @ResponseBody
    public TableReturnUtil listJson(TableUploadUtil tableUploadUtil)
    {
        TableReturnUtil tableReturnUtil = new TableReturnUtil();
        try
        {
            tableReturnUtil = this.sysResourceServive.listJson(tableUploadUtil);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return tableReturnUtil;
    }
    @RequestMapping({"/loadCheckAttributeIsExistence"})
    @ResponseBody
    public ResultResponse loadCheckAttributeIsExistence(String attribute,String checkVal,Long id){
        ResultResponse response = new ResultResponse();
        try
        {
            response = this.sysResourceServive.loadCheckAttributeIsExistence(attribute,checkVal,id);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            response.setFailureResult("不可用");
        }
        return response;
    }

    @RequestMapping({"/listResourceZTreeVoByType"})
    @ResponseBody
    public ResultResponse listResourceZTreeVoByType(Model model,int type)
    {
        return sysResourceServive.listResourceZTreeVoByType(type);
    }
    @RequestMapping({"/listZtreeByUserId"})
    public String listZtreeByUserId(Model model,Long userId)
    {
        model.addAttribute("userId",userId);
        return "manage/admin/resource/listZtreeByUserId";
    }
    @RequestMapping({"/listZtreeByUserIdJson"})
    @ResponseBody
    public ResultResponse listZtreeByUserIdJson(Model model,Long userId)
    {
        return sysResourceServive.listZtreeByUserId(userId);
    }
}
