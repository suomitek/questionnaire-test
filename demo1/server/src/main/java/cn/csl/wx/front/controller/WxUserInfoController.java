package cn.csl.wx.front.controller;

import cn.csl.basics.model.ResultResponse;
import cn.csl.basics.param.GlobalParam;
import cn.csl.wx.entity.WxUser;
import cn.csl.wx.service.WxUserService;
import cn.csl.wx.util.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * 微信支付
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/wx/front")
public class WxUserInfoController {
	@Resource
	private HttpSession httpSession;
	@Resource
	private WxUserService wxUserService;

	@RequestMapping({"/logindo"})
	@ResponseBody
	public ResultResponse logindo(String openid,HttpServletRequest request){
		ResultResponse response = wxUserService.loadByOpenid(openid);
		String schema = request.getScheme();
		String serverName = request.getServerName();
		// 端口号返回的是int类型
		int serverPort = request.getServerPort();
		String contextPath = request.getContextPath();
		String servletPath = request.getServletPath();
//		System.out.println("协议:"+ schema);
//		System.out.println("服务器名称:"+ serverName);
//		System.out.println("服务器端口号:"+ serverPort);
//		System.out.println("项目名称:"+ contextPath);
//		System.out.println("servlet路径:"+ servletPath);
		String loginUrl = schema+"://"+serverName+":"+serverPort+"/manage/front/logindo";
		if (response.isError()){
			response.setFailureResult("登录失败");
			return response;
		}
		WxUser dbWxUser = (WxUser)response.getData();
		JSONObject resultObject = HttpUtil.httpRequest(loginUrl, "GET", "id="+dbWxUser.getSubscriberId());
		response.setResultMsg(String.valueOf(resultObject.get("resultMsg")));
		response.setResultCode(String.valueOf(resultObject.get("resultCode")));
		response.setData(String.valueOf(resultObject.get("data")));
		return response;
	}
	@RequestMapping({"/logout"})
	@ResponseBody
	public ResultResponse loginOut(Model model){
		ResultResponse response = new ResultResponse();
		this.httpSession.setAttribute(GlobalParam.FRONT_SESSION, null);
		response.setSuccessResult("注销成功");
		return response;
	}
	@RequestMapping({"/register"})
	@ResponseBody
	public ResultResponse register(WxUser wxUser){
		ResultResponse response = new ResultResponse();
		try{
			response = wxUserService.addSave(wxUser);
		}catch (Exception e){
			e.printStackTrace();
			response.setFailureResult("注册失败");
		}
		return response;
	}

	@RequestMapping({"/getOpenId"})
	@ResponseBody
	public ResultResponse getOpenId(String code){
		ResultResponse response = new ResultResponse();
		try{
			response = wxUserService.getOpenId(code);
		}catch (Exception e){
			e.printStackTrace();
			response.setFailureResult("登录失败");
		}
		return response;
	}
	@RequestMapping({"/editInfo"})
	@ResponseBody
	public ResultResponse editInfo(WxUser wxUser){
		ResultResponse response = new ResultResponse();
		try{
			response = wxUserService.editSave(wxUser);
		}catch (Exception e){
			e.printStackTrace();
			response.setFailureResult("修改失败");
		}
		return response;
	}
	
}
