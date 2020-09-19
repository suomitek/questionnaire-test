package cn.csl.manage.admin.controller;

import cn.csl.manage.entity.SysEnvironment;
import cn.csl.basics.model.ResultResponse;
import cn.csl.manage.service.SysEnvironmentServive;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping({"/manage/admin/environment"})
public class AdminSysEnvironmentController
{
	@Resource
	private SysEnvironmentServive sysEnvironmentServive;

	@RequestMapping({"/edit"})
	public String login(Model model)
	{
		model.addAttribute("sysEnvironment",sysEnvironmentServive.load());
		return "manage/admin/environment/edit";
	}

	@RequestMapping({"/editSave"})
	@ResponseBody
	public ResultResponse editSave(Model model,SysEnvironment sysEnvironment)
	{
		ResultResponse response = new ResultResponse();
		try{
			response=sysEnvironmentServive.editSave(sysEnvironment);
		}catch (Exception e){
			e.printStackTrace();
			response.setFailureResult("修改失败");
		}
		return response;
	}
}
