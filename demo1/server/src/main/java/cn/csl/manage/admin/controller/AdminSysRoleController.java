package cn.csl.manage.admin.controller;

import cn.csl.manage.entity.SysRole;
import cn.csl.basics.model.ResultResponse;
import cn.csl.manage.service.SysRoleServive;
import cn.csl.basics.util.TableReturnUtil;
import cn.csl.basics.util.TableUploadUtil;
import cn.csl.basics.util.annotation.search.LoadSearchAnnotationAttribute;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping({"/manage/admin/role"})
public class AdminSysRoleController {
    @Resource
    private SysRoleServive sysRoleServive;

    @RequestMapping({"/add"})
    public String add()
    {
        return "manage/admin/role/add";
    }

    @RequestMapping({"/addSave"})
    @ResponseBody
    public ResultResponse addSave(SysRole sysRole)
    {
        ResultResponse response = new ResultResponse();
        try
        {
            response = this.sysRoleServive.addSave(sysRole);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            response.setFailureResult("添加失败");
        }
        return response;
    }

    @RequestMapping({"/edit"})
    public String edit(Model model, String sysRoleId)
    {
        model.addAttribute("sysRole", this.sysRoleServive.selectByPrimaryKey(sysRoleId));
        return "manage/admin/role/edit";
    }

    @RequestMapping({"/editSave"})
    @ResponseBody
    public ResultResponse editSave(SysRole role)
    {
        ResultResponse response = new ResultResponse();
        try
        {
            response = this.sysRoleServive.editSave(role);
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
    public ResultResponse delete(String roleId)
    {
        ResultResponse response = new ResultResponse();
        try
        {
            response = this.sysRoleServive.delete(roleId);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            response.setFailureResult("删除失败");
        }
        return response;
    }
    @RequestMapping({"/loadCheckAttributeIsExistence"})
    @ResponseBody
    public ResultResponse loadCheckAttributeIsExistence(String attribute,String checkVal,Long id)
    {
        ResultResponse response = new ResultResponse();
        try{

            response=sysRoleServive.loadCheckAttributeIsExistence(attribute,checkVal,id);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            response.setFailureResult("不可用");
        }
        return response;
    }
    @RequestMapping({"/list"})
    public String list(Model model)
    {
        model.addAttribute("map", LoadSearchAnnotationAttribute.getObjectValue(SysRole.class));
        return "manage/admin/role/list";
    }

    @RequestMapping({"/listJson"})
    @ResponseBody
    public TableReturnUtil listJson(TableUploadUtil tableUploadUtil)
    {
        TableReturnUtil tableReturnUtil = new TableReturnUtil();
        try
        {
            tableReturnUtil = this.sysRoleServive.listJson(tableUploadUtil);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return tableReturnUtil;
    }
    @RequestMapping({"/distribution"})
    public String distribution(Model model, String userId)
    {
        model.addAttribute("roleList", this.sysRoleServive.distribution(userId));
        model.addAttribute("userId", userId);
        return "manage/admin/role/distribution";
    }
    @RequestMapping({"/distributionSave"})
    @ResponseBody
    public ResultResponse distributionSave(Model model, String userId,String roles)
    {
        ResultResponse response = new ResultResponse();
        try{

            response=sysRoleServive.changeUserRole(userId,roles);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            response.setFailureResult("不可用");
        }
        return response;
    }
}
