package cn.csl.basics.intercept;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class SessionInterceptor
		extends HandlerInterceptorAdapter
{
	private String sessionType;

	public void setSessionType(String sessionType)
	{
		this.sessionType = sessionType;
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception
	{
		Object sessionObject = request.getSession().getAttribute("adminsession");
		if (sessionObject == null)
		{
			response.sendRedirect(request.getContextPath() + "/admin/login");
			return false;
		}
		return super.preHandle(request, response, handler);
	}
}
