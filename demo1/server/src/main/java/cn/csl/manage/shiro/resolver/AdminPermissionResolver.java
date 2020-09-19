package cn.csl.manage.shiro.resolver;

import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.Permission;

public class AdminPermissionResolver implements PermissionResolver {
    @Override
    public Permission resolvePermission(String permissionString) {
        return new AntPathPermission(permissionString);
    }
    protected class AntPathPermission implements Permission {
        private String permissionString;
        public AntPathPermission(String permissionString) {
            this.permissionString = permissionString;
        }
        public String getPermissionString() {
            return permissionString;
        }
        @Override
        public boolean implies(Permission p) {
            if (!(p instanceof AntPathPermission)) {
                return false;
            }
            AntPathPermission wp = (AntPathPermission) p;
            String wpPerm = wp.getPermissionString();
            if (StringUtils.equals(wpPerm, permissionString)) {
                return true;
            }
            return false;
        }
    }
}
