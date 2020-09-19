package cn.csl.manage.admin.controller;

import cn.csl.basics.model.ResultResponse;
import cn.csl.basics.model.UserSession;
import cn.csl.basics.param.GlobalParam;
import cn.csl.manage.service.SysEnvironmentServive;
import cn.csl.manage.service.SysResourceServive;
import cn.csl.manage.service.SysUserServive;
import cn.csl.manage.shiro.util.ShiroUtils;
import cn.csl.manage.admin.vo.SysResourceVo;
import cn.csl.manage.entity.SysUser;
import cn.csl.manage.shiro.token.UserPasswordToken;
import cn.csl.manage.shiro.type.LoginType;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping({"/manage/admin"})
public class AdminController
{
	@Resource
	private HttpSession httpSession;
	@Resource
	private SysUserServive sysUserServive;
	@Resource
	private SysResourceServive  sysResourceServive;


	@RequestMapping({"/welcome"})
	public String welcome(Model model)
	{
		return "manage/admin/welcome";
	}
	@RequestMapping({"/index"})
	public String index(Model model)
	{
		List<SysResourceVo> sysResourceVos = sysResourceServive.listResources();
		model.addAttribute("sysResourceVos", sysResourceVos);
		model.addAttribute("admin",sysUserServive.selectByPrimaryKey(((UserSession)httpSession.getAttribute(GlobalParam.ADMIN_SESSION)).getUserId()));
		return "manage/admin/index";
	}

	@RequestMapping({"/login"})
	public String login(Model model, HttpServletRequest request)
	{
		model.addAttribute("callback", "//manage//admin//index");
		return "manage/admin/login";
	}
	@RequestMapping({"/unauthorizedUrl"})
	public String unauthorizedUrl(Model model, HttpServletRequest request)
	{

		StringBuffer url = request.getRequestURL();
//		Subject subject = ShiroUtils.getSubject();
		if(request.getRequestURL().indexOf("admin")!=-1){
			return "redirect:/manage/admin/login";
		}
		if(request.getRequestURL().indexOf("front")!=-1){
			return "redirect:/manage/front/login";
		}
		return "redirect:/manage/admin/login";
	}

	@RequestMapping({"/logindo"})
	@ResponseBody
	public ResultResponse logindo(Model model, String name,String password){
		ResultResponse response = new ResultResponse();
		List<SysUser> sysUsers = sysUserServive.listByAttribute("name",name);
		if(sysUsers.size()<=0){
			response.setFailureResult("用户不存在");
			return response;
		}
		if("".equals(name)||"".equals(password)){
			response.setFailureResult("用户名和密码都不能为空");
			return response;
		}
		password = new Sha256Hash(password).toHex();
		if(!sysUsers.get(0).getPassword().equals(password)){
			response.setFailureResult("密码不正确");
			return response;
		}
		Subject subject = ShiroUtils.getSubject();
		//sha256加密
		UserPasswordToken token = new UserPasswordToken(name, password, LoginType.ADMIN.toString());
		subject.login(token);
		UserSession us = (UserSession)subject.getPrincipal();

//		UserSession us = new UserSession();
//		us.setUserId(user.getId().toString());
//		us.setUserName(user.getName());
//		us.setUserType(user.getUserType()+"");
//		us.setAdminId(user.getAdminId()+"");
		subject.getSession().setAttribute(GlobalParam.ADMIN_SESSION, us);
		this.httpSession.setAttribute(GlobalParam.ADMIN_SESSION, us);
		sysUserServive.setSession(us);
		response.setSuccessResult("登陆成功");
		return response;
	}

	@RequestMapping({"/logout"})
	@ResponseBody
	public ResultResponse loginOut(Model model)
	{
		ResultResponse response = new ResultResponse();
		this.httpSession.setAttribute(GlobalParam.ADMIN_SESSION, null);
		response.setSuccessResult("注销成功");
		return response;
	}
}
