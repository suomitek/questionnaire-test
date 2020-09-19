package cn.csl.manage.shiro.authenticator;

import cn.csl.manage.shiro.token.UserPasswordToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

import java.util.ArrayList;
import java.util.Collection;

/**
 *  自定义Authenticator
 * 注意，对应Realm的类名要分别包含字符串“admin”或“front”，
 *不能一个类名同时包含两个字符串，否则无法选择Realm
 */
public class ModelRealmAuthenticator extends ModularRealmAuthenticator {
    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        assertRealmsConfigured();
        UserPasswordToken customizedToken = (UserPasswordToken) authenticationToken;
        String loginType = customizedToken.getLoginType();
        Collection<Realm> realms = getRealms();
        Collection<Realm> loginRealms = new ArrayList<>();
        for (Realm realm : realms) {
            if (realm.getName().contains(loginType))
                loginRealms.add(realm);
        }
        return doMultiRealmAuthentication(loginRealms, customizedToken);
    }
}
