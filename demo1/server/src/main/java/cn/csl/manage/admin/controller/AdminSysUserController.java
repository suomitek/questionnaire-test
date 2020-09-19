package cn.csl.manage.admin.controller;

import cn.csl.manage.entity.SysUser;
import cn.csl.basics.model.ResultResponse;
import cn.csl.manage.service.SysUserServive;
import cn.csl.basics.util.TableReturnUtil;
import cn.csl.basics.util.TableUploadUtil;
import cn.csl.basics.util.annotation.search.LoadSearchAnnotationAttribute;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping({"/manage/admin/user"})
public class AdminSysUserController {
    @Resource
    private SysUserServive sysUserServive;


    @RequestMapping({"/add"})
    public String add(Model model)
    {
        return "manage/admin/user/add";
    }

    @RequestMapping({"/addSave"})
    @ResponseBody
    public ResultResponse addSave(SysUser sysUser)
    {
        ResultResponse response = new ResultResponse();
        try
        {
            sysUser.setAdminId(0L);
            response = this.sysUserServive.addSave(sysUser);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            response.setFailureResult("添加失败");
        }
        return response;
    }
    @RequestMapping({"/loadCheckAttributeIsExistence"})
    @ResponseBody
    public ResultResponse loadCheckAttributeIsExistence(String attribute,String checkVal,Long id)
    {
        ResultResponse response = new ResultResponse();
        try
        {
            response = this.sysUserServive.loadCheckAttributeIsExistence(attribute,checkVal,id);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            response.setFailureResult("不可用");
        }
        return response;
    }
    @RequestMapping({"/delete"})
    @ResponseBody
    public ResultResponse delete(String userId)
    {
        ResultResponse response = new ResultResponse();
        try
        {
            response = this.sysUserServive.delete(userId);
        }catch (Exception e)
        {
            e.printStackTrace();
            response.setFailureResult("删除失败");
        }
        return response;
    }
    @RequestMapping({"/changeUseFlag"})
    @ResponseBody
    public ResultResponse changeUseFlag(String userId)
    {
        ResultResponse response = new ResultResponse();
        try
        {
            response = this.sysUserServive.changeUseFlag(userId);
        }catch (Exception e)
        {
            e.printStackTrace();
            response.setFailureResult("操作失败");
        }
        return response;
    }
    @RequestMapping({"/edit"})
    public String edit(Model model, String userId)
    {
        model.addAttribute("user", this.sysUserServive.selectByPrimaryKey(userId));
        return "manage/admin/user/edit";
    }

    @RequestMapping({"/editSave"})
    @ResponseBody
    public ResultResponse editSave(SysUser sysUser)
    {
        ResultResponse response = new ResultResponse();
        try
        {
            response = this.sysUserServive.editSave(sysUser);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            response.setFailureResult("修改失败");
        }
        return response;
    }

    @RequestMapping({"/list"})
    public String list(Model model)
    {
        model.addAttribute("map", LoadSearchAnnotationAttribute.getObjectValue(SysUser.class));
        return "manage/admin/user/list";
    }

    @RequestMapping({"/listJson"})
    @ResponseBody
    public TableReturnUtil listJson(TableUploadUtil tableUploadUtil)
    {
        TableReturnUtil tableReturnUtil = new TableReturnUtil();
        try
        {
            tableReturnUtil = this.sysUserServive.listJson(tableUploadUtil);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return tableReturnUtil;
    }
    @RequestMapping({"/adminList"})
    public String adminList(Model model, String id)
    {
        model.addAttribute("id", id);
        model.addAttribute("users",sysUserServive.adminList());
        return "manage/admin/user/adminList";
    }
}
