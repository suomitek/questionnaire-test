package cn.csl.wenjuan.entity;

import cn.csl.basics.util.annotation.check.CheckAnnotation;

import java.util.Date;

public class WjComment {
    private Long id;

    private Date createTime;

    private Integer state;

    private String subscriberNature;
    private Long  subscriberId;
    private String  subscriberSurface;
    @CheckAnnotation(type = "required",attribute="评论内容")
    private String comment;

    private Long useless;

    private Long fabulous;

    private String surface;

    private String nature;

    private Long surfaceId;

    private String costSurface;

    private String costNature;

    private Long costSurfaceId;

    public String getSubscriberNature() {
        return subscriberNature;
    }

    public void setSubscriberNature(String subscriberNature) {
        this.subscriberNature = subscriberNature;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(Long subscriberId) {
        this.subscriberId = subscriberId;
    }

    public String getSubscriberSurface() {
        return subscriberSurface;
    }

    public void setSubscriberSurface(String subscriberSurface) {
        this.subscriberSurface = subscriberSurface;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    public Long getUseless() {
        return useless;
    }

    public void setUseless(Long useless) {
        this.useless = useless;
    }

    public Long getFabulous() {
        return fabulous;
    }

    public void setFabulous(Long fabulous) {
        this.fabulous = fabulous;
    }

    public String getSurface() {
        return surface;
    }

    public void setSurface(String surface) {
        this.surface = surface == null ? null : surface.trim();
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature == null ? null : nature.trim();
    }

    public Long getSurfaceId() {
        return surfaceId;
    }

    public void setSurfaceId(Long surfaceId) {
        this.surfaceId = surfaceId;
    }

    public String getCostSurface() {
        return costSurface;
    }

    public void setCostSurface(String costSurface) {
        this.costSurface = costSurface == null ? null : costSurface.trim();
    }

    public String getCostNature() {
        return costNature;
    }

    public void setCostNature(String costNature) {
        this.costNature = costNature == null ? null : costNature.trim();
    }

    public Long getCostSurfaceId() {
        return costSurfaceId;
    }

    public void setCostSurfaceId(Long costSurfaceId) {
        this.costSurfaceId = costSurfaceId;
    }
}