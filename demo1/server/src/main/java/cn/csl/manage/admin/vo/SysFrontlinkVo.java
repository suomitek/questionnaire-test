package cn.csl.manage.admin.vo;

import cn.csl.basics.util.annotation.search.SearchAnnotation;
import cn.csl.basics.util.annotation.unique.UniqueAnnotation;
import cn.csl.manage.entity.SysFrontlink;

import java.util.Date;

public class SysFrontlinkVo {
    private Long id;
    private Integer state;
    private Date createTime;
    private String linkUrl;
    private String linkName;
    private String linkCode;
    private boolean flag;

    public SysFrontlinkVo(SysFrontlink sysFrontlink) {
        this.id = sysFrontlink.getId();
        this.state = sysFrontlink.getState();
        this.createTime = sysFrontlink.getCreateTime();
        this.linkUrl = sysFrontlink.getLinkUrl();
        this.linkName = sysFrontlink.getLinkName();
        this.linkCode = sysFrontlink.getLinkCode();
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

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getLinkCode() {
        return linkCode;
    }

    public void setLinkCode(String linkCode) {
        this.linkCode = linkCode;
    }
}
