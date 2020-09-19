package cn.csl.manage.admin.vo;

import cn.csl.basics.util.annotation.search.SearchAnnotation;
import cn.csl.basics.util.annotation.unique.UniqueAnnotation;
import cn.csl.manage.entity.SysFrontrole;

import java.util.Date;

public class FrontRoleVo {
    private boolean flag;
    private Long id;
    private Integer state;
    private Date createTime;
    private String roleDesc;
    private String roleName;

    public FrontRoleVo(boolean flag, SysFrontrole sysFrontrole) {
        this.flag = flag;
        this.id = sysFrontrole.getId();
        this.state = sysFrontrole.getState();
        this.createTime = sysFrontrole.getCreateTime();
        this.roleDesc = sysFrontrole.getRoleDesc();
        this.roleName = sysFrontrole.getRoleName();
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
