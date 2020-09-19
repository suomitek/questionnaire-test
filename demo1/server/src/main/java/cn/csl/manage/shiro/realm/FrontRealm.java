package cn.csl.manage.shiro.realm;

import cn.csl.basics.model.UserSession;
import cn.csl.manage.service.SysSubscriderServive;
import cn.csl.manage.entity.*;
import cn.csl.manage.shiro.type.LoginType;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 自定义前台Realm .
 */
public class FrontRealm extends AuthorizingRealm {
    @Resource
    private SysSubscriderServive sysSubscriderServive;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        UserSession us = (UserSession)principals.getPrimaryPrincipal();
        Long userId = Long.valueOf(us.getUserId());

        List<SysFrontrole> roleList = null;
        List<SysFrontlink> resourceList = null;

        Set<String> roleSet = new HashSet<>();
        Set<String> permissionSet = new HashSet<>();

        if (us.getLoginType().equals(LoginType.FRONT.toString())){
            roleList = sysSubscriderServive.listRolesBySubscriderId(userId);
            for (SysFrontrole role:roleList) {
                roleSet.add(role.getRoleName().toString());
            }
            /**
             * 获取用户所有权限
             */
            resourceList = sysSubscriderServive.listSubLinks(roleList);
            for(SysFrontlink resource:resourceList){
                permissionSet.add(resource.getLinkUrl());
            }
        }

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setRoles(roleSet);
        info.setStringPermissions(permissionSet);
        return info;
    }
    @Override
    // 验证的核心方法
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        String password = new String((char[]) token.getCredentials());
        //查询用户信息
        List<SysSubscriber> users = sysSubscriderServive.listByAttribute("name",username);
        //账号不存在
        if(users.size()<1){
            throw new UnknownAccountException("账号或密码不正确");
        }
        UserSession us = new UserSession();
        us.setUserId(users.get(0).getId().toString());
        us.setUserName(users.get(0).getName());
        us.setUserPassword(new Sha256Hash(users.get(0).getName()).toHex());
        us.setLoginType(LoginType.FRONT.toString());
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
