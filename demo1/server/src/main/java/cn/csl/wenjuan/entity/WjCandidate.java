package cn.csl.wenjuan.entity;

import java.util.Date;
import java.util.List;

public class WjCandidate {
    private Long id;
    private String comment;

    private Integer state;

    private Date createTime;

    private Integer orderNum;

    private String cover;
    private List<Long> coverId;

    private String imgIntroduce;
    private List<Long> imgIntroduceId;

    private String introduce;

    private String name;

    private Integer supNum;

    private Integer useFlag;

    private Long voteId;

    private Integer voteType;

    private Long wxUserId;

    public WjCandidate() {
        this.state = 1;
        this.createTime = new Date();
        this.supNum = 0;
        this.useFlag = 1;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<Long> getImgIntroduceId() {
        return imgIntroduceId;
    }

    public void setImgIntroduceId(List<Long> imgIntroduceId) {
        this.imgIntroduceId = imgIntroduceId;
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

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getImgIntroduce() {
        return imgIntroduce;
    }

    public void setImgIntroduce(String imgIntroduce) {
        this.imgIntroduce = imgIntroduce;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce == null ? null : introduce.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getSupNum() {
        return supNum;
    }

    public void setSupNum(Integer supNum) {
        this.supNum = supNum;
    }

    public Integer getUseFlag() {
        return useFlag;
    }

    public void setUseFlag(Integer useFlag) {
        this.useFlag = useFlag;
    }

    public Long getVoteId() {
        return voteId;
    }

    public void setVoteId(Long voteId) {
        this.voteId = voteId;
    }

    public Integer getVoteType() {
        return voteType;
    }

    public void setVoteType(Integer voteType) {
        this.voteType = voteType;
    }

    public Long getWxUserId() {
        return wxUserId;
    }

    public void setWxUserId(Long wxUserId) {
        this.wxUserId = wxUserId;
    }

    public List<Long>  getCoverId() {
        return coverId;
    }

    public void setCoverId(List<Long>  coverId) {
        this.coverId = coverId;
    }
}