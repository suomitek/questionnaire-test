package cn.csl.manage.shiro.matcher;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

//自定义matcher,这里就是对比用户的输入的信息封装成的token和按照用户输入的principal(一般就是用户名)从数据库中查询出的信息封装的info信息,一般就是比对他们的Credentials
public class MyCredentialsMatcher extends SimpleCredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        Object tokenCredentials = getCredentials(token);
        Object accountCredentials = getCredentials(info);
        return super.equals(tokenCredentials, accountCredentials);
    }
}