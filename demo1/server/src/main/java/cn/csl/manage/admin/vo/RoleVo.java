package cn.csl.manage.admin.vo;


import cn.csl.manage.entity.SysRole;
import org.apache.commons.beanutils.BeanUtils;

public class RoleVo extends SysRole {
    private boolean flag;

    public RoleVo(SysRole sysRole,boolean flag){
        try {
            BeanUtils.copyProperties(this, sysRole);
            this.flag=flag;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
