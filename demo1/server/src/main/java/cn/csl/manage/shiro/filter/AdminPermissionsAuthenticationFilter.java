package cn.csl.manage.shiro.filter;

import cn.csl.manage.shiro.token.UserPasswordToken;
import cn.csl.manage.shiro.type.LoginType;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 自定义webform表单认证过滤器<br/>
 */
public class AdminPermissionsAuthenticationFilter extends PermissionsAuthorizationFilter {
    public static final String LOGIN_TYPE = LoginType.ADMIN.toString();

    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)throws IOException {
        mappedValue = buildPermissionsFromRequest(request);
        boolean flag = super.isAccessAllowed(request, response,mappedValue );
        return flag;
    }
    protected String[] buildPermissionsFromRequest(ServletRequest request) {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        String uri = servletRequest.getRequestURI();
        return new String[] { uri };//返回请求URI
    }
}
