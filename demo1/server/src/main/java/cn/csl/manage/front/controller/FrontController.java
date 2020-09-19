package cn.csl.manage.front.controller;

import cn.csl.basics.model.ResultResponse;
import cn.csl.basics.model.UserSession;
import cn.csl.basics.param.GlobalParam;
import cn.csl.manage.service.SysEnvironmentServive;
import cn.csl.manage.service.SysSubscriderServive;
import cn.csl.manage.entity.SysSubscriber;
import cn.csl.manage.shiro.token.UserPasswordToken;
import cn.csl.manage.shiro.type.LoginType;
import cn.csl.manage.shiro.util.ShiroUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping({"/manage/front"})
public class FrontController
{
	@Resource
	private HttpSession httpSession;
	@Resource
	private SysSubscriderServive sysSubscriderServive;
	@Resource
	private SysEnvironmentServive sysEnvironmentServive;

	@RequestMapping({"/login"})
	@ResponseBody
	public ResultResponse login(Model model, HttpServletRequest request)
	{
		ResultResponse response = new ResultResponse();
		response.setResultCode("401");
		response.setResultMsg("请登录");
		return response;
	}

	@RequestMapping({"/logindo"})
	@ResponseBody
	public ResultResponse logindo(Model model, Long id){
		ResultResponse response = new ResultResponse();
		SysSubscriber sysSubscriber = sysSubscriderServive.selectByPrimaryKey(id);
		if(sysSubscriber==null){
			response.setFailureResult("用户不存在");
			return response;
		}
		if(sysSubscriber.getUseFlag()==0){
			response.setFailureResult("用户被锁定");
			return response;
		}
		Subject subject = ShiroUtils.getSubject();
		//sha256加密
		UserPasswordToken token = new UserPasswordToken(sysSubscriber.getName(), new Sha256Hash(sysSubscriber.getName()).toHex(), LoginType.FRONT.toString());
		subject.login(token);
		UserSession us = (UserSession)subject.getPrincipal();
		String sessionId = (String) subject.getSession().getId();
		subject.getSession().setAttribute(GlobalParam.FRONT_SESSION, us);
		this.httpSession.setAttribute(GlobalParam.FRONT_SESSION, us);
		response.setResultMsg("登录成功");
		response.setSuccessResult(sessionId);
		return response;
	}

	@RequestMapping({"/logout"})
	@ResponseBody
	public ResultResponse loginOut(Model model)
	{
		ResultResponse response = new ResultResponse();
		this.httpSession.setAttribute(GlobalParam.FRONT_SESSION, null);
		response.setSuccessResult("注销成功");
		return response;
	}
	@RequestMapping({"/introduceVideo"})
	public String introduceVideo(Model model, HttpServletRequest request)
	{
		model.addAttribute("introduceVideoUrl", sysEnvironmentServive.load().getIntroduceVideoUrl());
		return "manage/front/introduceVideo";
	}
}
