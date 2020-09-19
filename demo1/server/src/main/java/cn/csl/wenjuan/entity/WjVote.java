package cn.csl.wenjuan.entity;

import cn.csl.basics.util.RandomUtil;

import java.util.Date;

public class WjVote {
    private Long id;

    private Long commentNum;//总评论数 不存数据库

    private Integer state;

    private Date createTime;

    private String name;

    private String cover;

    private String introduce;

    private String themeColor;

    private String imgIntroduce;

    private Integer repeatFlag;

    private Integer frequency;

    private Integer restrictFlag;
    private Integer suspend;

    private String province;

    private String city;

    private String district;

    private Integer ipWxUserFrequency;

    private Integer overt;

    private Integer comment;

    private Integer outside;

    private Integer sponsor;

    private String sponsorName;

    private String sponsorPhone;

    private Integer reward;

    private String rewardDesc;

    private String rewarImg;

    private Date startTime;

    private Date endTime;

    private Integer voteType;

    private Integer unusual;

    private String unusualEplain;

    private String reportEplain;

    private Long rWxUserId;

    private Long wxUserId;

    private Integer candidateNum;

    private Integer candidateIndex;

    private Integer supNum;

    private String voteDataStr;
    private String voteCode;

    public WjVote(){
        this.createTime = new Date();
        this.state = 1;
        this.themeColor  = "#fff";
        this.repeatFlag = 0;
        this.frequency = 1;
        this.restrictFlag = 0;
        this.province = "不限";
        this.city = "不限";
        this.district = "不限";
        this.ipWxUserFrequency = 1;
        this.overt = 0;
        this.suspend = 1;//不暂停
        this.comment = 1;
        this.outside = 1;
        this.sponsor = 0;
        this.reward = 0;
        this.voteType = 2;
        this.unusual = 1;
        this.candidateNum = 0;
        this.candidateIndex = 0;
        this.supNum = 0;
    }

    public Long getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Long commentNum) {
        this.commentNum = commentNum;
    }

    public String getVoteCode() {
        return voteCode;
    }

    public void setVoteCode(String voteCode) {
        this.voteCode = voteCode;
    }

    public String getVoteDataStr() {
        return voteDataStr;
    }

    public void setVoteDataStr(String voteDataStr) {
        this.voteDataStr = voteDataStr;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover == null ? null : cover.trim();
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce == null ? null : introduce.trim();
    }

    public String getThemeColor() {
        return themeColor;
    }

    public void setThemeColor(String themeColor) {
        this.themeColor = themeColor == null ? null : themeColor.trim();
    }

    public String getImgIntroduce() {
        return imgIntroduce;
    }

    public void setImgIntroduce(String imgIntroduce) {
        this.imgIntroduce = imgIntroduce == null ? null : imgIntroduce.trim();
    }

    public Integer getRepeatFlag() {
        return repeatFlag;
    }

    public void setRepeatFlag(Integer repeatFlag) {
        this.repeatFlag = repeatFlag;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public Integer getRestrictFlag() {
        return restrictFlag;
    }

    public void setRestrictFlag(Integer restrictFlag) {
        this.restrictFlag = restrictFlag;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district == null ? null : district.trim();
    }

    public Integer getIpWxUserFrequency() {
        return ipWxUserFrequency;
    }

    public void setIpWxUserFrequency(Integer ipWxUserFrequency) {
        this.ipWxUserFrequency = ipWxUserFrequency;
    }

    public Integer getOvert() {
        return overt;
    }

    public void setOvert(Integer overt) {
        this.overt = overt;
    }

    public Integer getComment() {
        return comment;
    }

    public void setComment(Integer comment) {
        this.comment = comment;
    }

    public Integer getOutside() {
        return outside;
    }

    public void setOutside(Integer outside) {
        this.outside = outside;
    }

    public Integer getSponsor() {
        return sponsor;
    }

    public void setSponsor(Integer sponsor) {
        this.sponsor = sponsor;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName == null ? null : sponsorName.trim();
    }

    public String getSponsorPhone() {
        return sponsorPhone;
    }

    public void setSponsorPhone(String sponsorPhone) {
        this.sponsorPhone = sponsorPhone == null ? null : sponsorPhone.trim();
    }

    public Integer getReward() {
        return reward;
    }

    public void setRewar(Integer reward) {
        this.reward = reward;
    }

    public String getRewardDesc() {
        return rewardDesc;
    }

    public void setRewardDesc(String rewardDesc) {
        this.rewardDesc = rewardDesc == null ? null : rewardDesc.trim();
    }

    public String getRewarImg() {
        return rewarImg;
    }

    public void setRewarImg(String rewarImg) {
        this.rewarImg = rewarImg == null ? null : rewarImg.trim();
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getVoteType() {
        return voteType;
    }

    public void setVoteType(Integer voteType) {
        this.voteType = voteType;
    }

    public Integer getUnusual() {
        return unusual;
    }

    public void setUnusual(Integer unusual) {
        this.unusual = unusual;
    }

    public String getUnusualEplain() {
        return unusualEplain;
    }

    public void setUnusualEplain(String unusualEplain) {
        this.unusualEplain = unusualEplain == null ? null : unusualEplain.trim();
    }

    public String getReportEplain() {
        return reportEplain;
    }

    public void setReportEplain(String reportEplain) {
        this.reportEplain = reportEplain == null ? null : reportEplain.trim();
    }

    public Long getrWxUserId() {
        return rWxUserId;
    }

    public void setrWxUserId(Long rWxUserId) {
        this.rWxUserId = rWxUserId;
    }

    public Long getWxUserId() {
        return wxUserId;
    }

    public void setWxUserId(Long wxUserId) {
        this.wxUserId = wxUserId;
    }

    public Integer getCandidateNum() {
        return candidateNum;
    }

    public void setCandidateNum(Integer candidateNum) {
        this.candidateNum = candidateNum;
    }

    public Integer getCandidateIndex() {
        return candidateIndex;
    }

    public void setCandidateIndex(Integer candidateIndex) {
        this.candidateIndex = candidateIndex;
    }

    public Integer getSupNum() {
        return supNum;
    }

    public void setSupNum(Integer supNum) {
        this.supNum = supNum;
    }

    public Integer getSuspend() {
        return suspend;
    }

    public void setSuspend(Integer suspend) {
        this.suspend = suspend;
    }
}