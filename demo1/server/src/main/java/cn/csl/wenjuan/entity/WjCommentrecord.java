package cn.csl.wenjuan.entity;

import java.util.Date;

public class WjCommentrecord {
    private Long id;

    private Integer state;

    private Date createTime;

    private Long subscriberId;

    private String subscriberSurface;

    private Integer useless;

    private Integer fabulous;

    private Long surfaceId;

    private String surface;

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
        this.subscriberSurface = subscriberSurface == null ? null : subscriberSurface.trim();
    }

    public Integer getUseless() {
        return useless;
    }

    public void setUseless(Integer useless) {
        this.useless = useless;
    }

    public Integer getFabulous() {
        return fabulous;
    }

    public void setFabulous(Integer fabulous) {
        this.fabulous = fabulous;
    }

    public Long getSurfaceId() {
        return surfaceId;
    }

    public void setSurface(String surface) {
        this.surface = surface;
    }

    public String getSurface() {
        return surface;
    }

    public void setSurfaceId(Long surfaceId) {
        this.surfaceId = surfaceId;
    }
}