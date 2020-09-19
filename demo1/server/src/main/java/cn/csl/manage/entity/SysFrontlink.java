package cn.csl.manage.entity;

import cn.csl.basics.util.annotation.search.SearchAnnotation;
import cn.csl.basics.util.annotation.unique.UniqueAnnotation;

import java.util.Date;

public class SysFrontlink {
    private Long id;

    private Integer state;

    private Date createTime;
    @UniqueAnnotation(uniqueMsg = "URL地址已被使用")
    private String linkUrl;
    @SearchAnnotation(searchMsg = "URL名称")
    @UniqueAnnotation(uniqueMsg = "URL名称已被使用")
    private String linkName;
    @SearchAnnotation(searchMsg = "URL编码")
    @UniqueAnnotation(uniqueMsg = "URL编码已被使用")
    private String linkCode;

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
