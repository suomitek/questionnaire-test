package cn.csl.manage.shiro.realm;

import cn.csl.basics.model.UserSession;
import cn.csl.manage.redis.JedisUtil;
import cn.csl.manage.service.SysApiServive;
import cn.csl.manage.service.SysUserServive;
import cn.csl.manage.entity.SysApi;
import cn.csl.manage.entity.SysResource;
import cn.csl.manage.entity.SysRole;
import cn.csl.manage.entity.SysUser;
import cn.csl.manage.shiro.type.LoginType;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 自定义后台realm .
 *
 */
public class AdminRealm extends AuthorizingRealm {

    @Resource
    private SysUserServive sysUserSerivce;
    @Autowired
    private JedisUtil jedisUtil;

    // 授权回调函数
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        UserSession us = (UserSession)principals.getPrimaryPrincipal();
        Long userId = Long.valueOf(us.getUserId());
        Set<String> permissionSet = new HashSet<>();
        Set<String> roleSet = new HashSet<>();
        Boolean flag = true;
        if (us.getLoginType().equals(LoginType.ADMIN.toString())){
            String key = "session_"+LoginType.ADMIN.toString()+"_"+us.getUserName()+"_"+us.getUserPassword();
            flag = jedisUtil.KEYS.exists(key+"_roleSet")&&jedisUtil.KEYS.exists(key+"_permissionSet");
            if(flag){//如果数据库有
                roleSet = jedisUtil.SETS.smembers(key+"_roleSet");
                permissionSet = jedisUtil.SETS.smembers(key+"_permissionSet");
            }else{//没有就加入缓存
                sysUserSerivce.setSession(us);
                roleSet = jedisUtil.SETS.smembers(key+"_roleSet");
                permissionSet = jedisUtil.SETS.smembers(key+"_permissionSet");
            }
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setRoles(roleSet);
        info.setStringPermissions(permissionSet);
        return info;
    }
    /**
     * 认证操作，判断一个请求是否被允许进入系统 (回调用)
     * 认证回调函数
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        String password = new String((char[]) token.getCredentials());
        //查询用户信息
        List<SysUser> users = sysUserSerivce.listByAttribute("name",username);
        SysUser user = users.get(0);
        //账号不存在
        if(user == null) {
            throw new UnknownAccountException("账号或密码不正确");
        }
        //密码错误
        if(!password.equals(user.getPassword())) {
            throw new IncorrectCredentialsException("账号或密码不正确");
        }

        UserSession us = new UserSession();
        us.setUserId(user.getId().toString());
        us.setUserName(user.getName());
        us.setUserPassword(user.getPassword());
        us.setLoginType(LoginType.ADMIN.toString());
        us.setUserType(user.getUserType()+"");
		us.setAdminId(user.getAdminId()+"");
        //前台不用密码
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(us, password, getName());
        return info;
    }

    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }
    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }
}
