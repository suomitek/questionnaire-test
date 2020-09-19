package cn.csl.basics.intercept;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.csl.manage.service.SysEnvironmentServive;
import cn.csl.manage.service.SysResourceServive;
import cn.csl.manage.entity.SysResource;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class BasePathInterceptor extends HandlerInterceptorAdapter
{
	@Resource
	private SysEnvironmentServive sysEnvironmentServive;
	@Resource
	private SysResourceServive sysResourceServive;
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception
	{
		String url = request.getRequestURI();
//		request.setAttribute("basePath", request.getContextPath());
		request.setAttribute("environment", sysEnvironmentServive.load());

		String pattern = ".*/admin/.*";

		if(Pattern.matches(pattern, url)){
			request.setAttribute("pathTitle", getPath(url));
		}
		return true;
	}

	private String getPath(String url){
		List<SysResource> resources = sysResourceServive.listByAttribute("resUrl",url);
		List<SysResource> thisPaths = new ArrayList<>();
		String path="首页";
		if (resources.size()>0){
			SysResource sysResource = resources.get(0);
			thisPaths.add(sysResource);
			while (sysResource.getParentId()!=0){
				sysResource = sysResourceServive.loadByAttribute("id",String.valueOf(sysResource.getParentId()));
				thisPaths.add(sysResource);
			}
		}
		for (int i=thisPaths.size()-1;i>-1;i--){
			path+=">"+thisPaths.get(i).getResName();
		}
		return path;
	}
}
