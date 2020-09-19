package cn.csl.manage.entity;

import cn.csl.basics.util.annotation.check.CheckAnnotation;
import cn.csl.basics.util.annotation.search.SearchAnnotation;
import cn.csl.basics.util.annotation.unique.UniqueAnnotation;

public class SysRole {
    private Long id;
    @UniqueAnnotation(uniqueMsg = "角色名称已被使用")
    @SearchAnnotation(searchMsg = "角色名称")
    @CheckAnnotation(type = "required",attribute="角色名称")
    private String roleName;
    @SearchAnnotation(searchMsg = "角色描述")
    @CheckAnnotation(type = "required",attribute="角色描述")
    private String roleDesc;

    private Integer state;

    private Integer roleType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc == null ? null : roleDesc.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }
}