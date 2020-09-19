package cn.csl.manage.admin.vo;

import cn.csl.manage.entity.SysUser;
import cn.csl.basics.util.DatetimeUtils;
import org.apache.commons.beanutils.BeanUtils;

public class SysUserVo extends SysUser {
    public String createTimeStr;

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public SysUserVo(SysUser sysUser){
        try {
            BeanUtils.copyProperties(this, sysUser);
        }catch (Exception e){
            e.printStackTrace();
        }
        this.setPassword("");
        this.createTimeStr = DatetimeUtils.dateToString(sysUser.getCreateTime(),"yyyy-MM-dd");
    }
}
