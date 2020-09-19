package cn.csl.wenjuan.entity;

import cn.csl.basics.util.annotation.check.CheckAnnotation;
import cn.csl.basics.util.annotation.search.SearchAnnotation;
import cn.csl.basics.util.annotation.unique.UniqueAnnotation;

import java.util.Date;

public class WjBanner {
    private Long id;

    private Integer bannerType;

    private Date createTime;

    private String imagePath;

    private String linkUrl;
    @CheckAnnotation(type = "digits",attribute="排序号")
    private Integer orderNum;

    private Integer state;
    @UniqueAnnotation(uniqueMsg = "标题已被使用")
    @SearchAnnotation(searchMsg = "标题")
    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getBannerType() {
        return bannerType;
    }

    public void setBannerType(Integer bannerType) {
        this.bannerType = bannerType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath == null ? null : imagePath.trim();
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl == null ? null : linkUrl.trim();
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }
}