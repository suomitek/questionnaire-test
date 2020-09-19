package cn.csl.manage.entity;

import java.util.Date;

public class SysSubscriberRole {
    private Long id;

    private Integer state;

    private Date createTime;

    private Long scriberId;

    private Long roleId;

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

    public Long getScriberId() {
        return scriberId;
    }

    public void setScriberId(Long scriberId) {
        this.scriberId = scriberId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}