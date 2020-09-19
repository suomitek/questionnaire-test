package cn.csl.wenjuan.entity;

import java.util.Date;

public class WjQuestion {
    private Long id;

    private Date createTime;

    private int state;

    private String name;

    private String cover;

    private String introduce;

    private String imgIntroduce;

    private String themeColor;

    private int restrictFlag;

    private String province;

    private String city;

    private String district;

    private int overt;

    private int sponsor;

    private String sponsorName;

    private String sponsorPhone;

    private String voteCode;

    private int suspend;

    private Long wxUserId;

    private Date startTime;

    private Date endTime;

    private int problemNumber;

    private int answerNumber;

    private int unusual;

    private String unusualEplain;

    private String reportEplain;

    private Long rWxUserId;

    private int sumScore;

    private int examineFlag;
    private int ipWxUserFrequency;
    private int sendFlag;
    private String mailbox;

    private String questionDataStr;
    private String problemsStr;
    private String cproblemsStr;
    private String zipPath;
    private String zipCode;

    public String getMailbox() {
        return mailbox;
    }

    public void setSendFlag(int sendFlag) {
        this.sendFlag = sendFlag;
    }

    public String getCproblemsStr() {
        return cproblemsStr;
    }

    public void setCproblemsStr(String cproblemsStr) {
        this.cproblemsStr = cproblemsStr;
    }

    public int getSendFlag() {
        return sendFlag;
    }

    public void setMailbox(String mailbox) {
        this.mailbox = mailbox;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getZipPath() {
        return zipPath;
    }

    public void setZipPath(String zipPath) {
        this.zipPath = zipPath;
    }

    public String getProblemsStr() {
        return problemsStr;
    }

    public void setProblemsStr(String problemsStr) {
        this.problemsStr = problemsStr;
    }

    public int getIpWxUserFrequency() {
        return ipWxUserFrequency;
    }

    public void setIpWxUserFrequency(int ipWxUserFrequency) {
        this.ipWxUserFrequency = ipWxUserFrequency;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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

    public String getImgIntroduce() {
        return imgIntroduce;
    }

    public void setImgIntroduce(String imgIntroduce) {
        this.imgIntroduce = imgIntroduce == null ? null : imgIntroduce.trim();
    }

    public String getThemeColor() {
        return themeColor;
    }

    public void setThemeColor(String themeColor) {
        this.themeColor = themeColor == null ? null : themeColor.trim();
    }

    public int getRestrictFlag() {
        return restrictFlag;
    }

    public void setRestrictFlag(int restrictFlag) {
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

    public int getOvert() {
        return overt;
    }

    public void setOvert(int overt) {
        this.overt = overt;
    }

    public int getSponsor() {
        return sponsor;
    }

    public void setSponsor(int sponsor) {
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

    public String getVoteCode() {
        return voteCode;
    }

    public void setVoteCode(String voteCode) {
        this.voteCode = voteCode == null ? null : voteCode.trim();
    }

    public int getSuspend() {
        return suspend;
    }

    public void setSuspend(int suspend) {
        this.suspend = suspend;
    }

    public Long getWxUserId() {
        return wxUserId;
    }

    public void setWxUserId(Long wxUserId) {
        this.wxUserId = wxUserId;
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

    public int getProblemNumber() {
        return problemNumber;
    }

    public void setProblemNumber(int problemNumber) {
        this.problemNumber = problemNumber;
    }

    public int getAnswerNumber() {
        return answerNumber;
    }

    public void setAnswerNumber(int answerNumber) {
        this.answerNumber = answerNumber;
    }

    public int getUnusual() {
        return unusual;
    }

    public void setUnusual(int unusual) {
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

    public int getSumScore() {
        return sumScore;
    }

    public void setSumScore(int sumScore) {
        this.sumScore = sumScore;
    }

    public int getExamineFlag() {
        return examineFlag;
    }

    public void setExamineFlag(int examineFlag) {
        this.examineFlag = examineFlag;
    }

    public String getQuestionDataStr() {
        return questionDataStr;
    }

    public void setQuestionDataStr(String questionDataStr) {
        this.questionDataStr = questionDataStr == null ? null : questionDataStr.trim();
    }
}