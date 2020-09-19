package cn.csl.wenjuan.front.dto;

import cn.csl.basics.entity.BasicsFile;
import cn.csl.basics.util.DatetimeUtils;
import cn.csl.wenjuan.entity.WjQuestion;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.beanutils.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SettingDto {
    private List<String> covers= new ArrayList<>();//封面
    private List<Long> coverIds= new ArrayList<>();//
    private List<String> imgIntroduces = new ArrayList<>();//介绍
    private List<Long> imgIntroduceIds= new ArrayList<>();//
    private String themeColor;//主题色
    private int restrictFlag;//地区限制
    private int overt;//默认公开
    private int sponsor;//主办方
    private int comment;//开启评论
    private int outside;//不开放报名
    private int ipWxUserFrequency;//每个ip用户个数
    private int examineFlag;//问卷类型 1考试卷

    private String name;
    private String introduce;
    private String imgIntroduce;
    private String province;
    private String city;
    private String district;
    private String sponsorName;
    private String sponsorPhone;

    private List<Integer> numList= new ArrayList<>();
    private Integer numIndex;
    private Integer ipIndex;
    private Integer startYear;
    private Integer endYear;
    private String startTimeStr;
    private String endTimeStr;
    private String zipPath;
    private String zipCode;
    private int sendFlag;//是否自动发送消息
    private String mailbox;

    public SettingDto(String settingStr){
        JSONObject settingData = JSONObject.parseObject(settingStr);
        JSONArray coverIds = settingData.getJSONArray("coverId");
        for (int i=0;i<coverIds.size();i++) {
            this.coverIds.add(coverIds.getLong(i));
        }

        JSONArray coverImgs = settingData.getJSONArray("cover");
        for (int i=0;i<coverImgs.size();i++) {
            this.covers.add(coverImgs.getString(i));
        }
        this.imgIntroduces = imgIntroduces;
        this.imgIntroduceIds = imgIntroduceIds;

        JSONArray imgIntroduceIds = settingData.getJSONArray("imgIntroduceId");
        for (int i=0;i<imgIntroduceIds.size();i++) {
            this.imgIntroduceIds.add(imgIntroduceIds.getLong(i));
        }

        JSONArray imgIntroduceImgs = settingData.getJSONArray("imgIntroduce");
        for (int i=0;i<imgIntroduceImgs.size();i++) {
            this.imgIntroduces.add(imgIntroduceImgs.getString(i));
        }

        this.sendFlag = settingData.getInteger("sendFlag");
        this.mailbox = settingData.getString("mailbox");
        this.imgIntroduce = settingData.getString("imgIntroduce");
        this.introduce = settingData.getString("introduce");
        this.name = settingData.getString("name");
        this.province = settingData.getString("province");
        this.city = settingData.getString("city");
        this.district = settingData.getString("district");
        this.sponsorName = settingData.getString("sponsorName");
        this.sponsorPhone = settingData.getString("sponsorPhone");
        this.themeColor = settingData.getString("themeColor");
        this.restrictFlag = settingData.getInteger("restrictFlag");
        this.overt = settingData.getInteger("overt");
        this.sponsor = settingData.getInteger("sponsor");
        this.comment = settingData.getInteger("comment");
        this.outside = settingData.getInteger("outside");
        this.ipWxUserFrequency = settingData.getInteger("ipWxUserFrequency");
        this.examineFlag = settingData.getInteger("examineFlag");
        JSONArray numLists = settingData.getJSONArray("numList");
        for (int i=0;i<numLists.size();i++) {
            this.numList.add(numLists.getInteger(i));
        }
        this.numIndex = settingData.getInteger("numIndex");
        this.ipIndex = settingData.getInteger("ipIndex");
        this.startYear = settingData.getInteger("startYear");
        this.endYear = settingData.getInteger("endYear");
        this.startTimeStr = settingData.getString("startTimeStr");
        this.endTimeStr = settingData.getString("endTimeStr");
    }

    public WjQuestion getQuestion(){
        WjQuestion question = new WjQuestion();
        try {
            BeanUtils.copyProperties(question, this);
        }catch (Exception e){
            e.printStackTrace();
        }
//        question.setZipCode("");
//        question.setZipPath("");
        question.setAnswerNumber(0);//答卷人数
        question.setState(1);
        question.setSumScore(0);
        question.setCreateTime(new Date());
        question.setImgIntroduce(this.imgIntroduceIds.toString());
        question.setCover(this.coverIds.toString());
        question.setExamineFlag(this.examineFlag);
        question.setSuspend(0);//未暂停
        question.setIntroduce(this.introduce);
        try {
            question.setStartTime(new Date(Long.valueOf(this.startTimeStr)));
            question.setEndTime(new Date(Long.valueOf(this.endTimeStr)));
        }catch (Exception e){

        }
        return question;
    }

    public int getSendFlag() {
        return sendFlag;
    }

    public void setMailbox(String mailbox) {
        this.mailbox = mailbox;
    }

    public String getMailbox() {
        return mailbox;
    }

    public void setSendFlag(int sendFlag) {
        this.sendFlag = sendFlag;
    }

    public String getSettingStr(String settingStr, WjQuestion question){
        JSONObject settingJSON =  JSONObject.parseObject(settingStr);
        settingJSON.put("id",question.getId());
        settingJSON.put("voteCode",question.getVoteCode());
        settingJSON.put("wxUserId",question.getWxUserId());
        settingJSON.put("problemNumber",question.getProblemNumber());
        settingJSON.put("answerNumber",question.getAnswerNumber());
        settingJSON.put("introduce",question.getIntroduce());
        settingJSON.put("ipWxUserFrequency",question.getIpWxUserFrequency());
        return settingJSON.toJSONString();
    }

    public String getIntroduce() {
        return introduce;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setZipPath(String zipPath) {
        this.zipPath = zipPath;
    }

    public String getZipPath() {
        return zipPath;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public List<String> getCovers() {
        return covers;
    }

    public void setCovers(List<String> covers) {
        this.covers = covers;
    }

    public List<Long> getCoverIds() {
        return coverIds;
    }

    public void setCoverIds(List<Long> coverIds) {
        this.coverIds = coverIds;
    }

    public List<String> getImgIntroduces() {
        return imgIntroduces;
    }

    public void setImgIntroduces(List<String> imgIntroduces) {
        this.imgIntroduces = imgIntroduces;
    }

    public List<Long> getImgIntroduceIds() {
        return imgIntroduceIds;
    }

    public void setImgIntroduceIds(List<Long> imgIntroduceIds) {
        this.imgIntroduceIds = imgIntroduceIds;
    }

    public String getThemeColor() {
        return themeColor;
    }

    public void setThemeColor(String themeColor) {
        this.themeColor = themeColor;
    }

    public int getRestrictFlag() {
        return restrictFlag;
    }

    public void setRestrictFlag(int restrictFlag) {
        this.restrictFlag = restrictFlag;
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

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public int getOutside() {
        return outside;
    }

    public void setOutside(int outside) {
        this.outside = outside;
    }

    public int getIpWxUserFrequency() {
        return ipWxUserFrequency;
    }

    public void setIpWxUserFrequency(int ipWxUserFrequency) {
        this.ipWxUserFrequency = ipWxUserFrequency;
    }

    public int getExamineFlag() {
        return examineFlag;
    }

    public void setExamineFlag(int examineFlag) {
        this.examineFlag = examineFlag;
    }

    public List<Integer> getNumList() {
        return numList;
    }

    public void setNumList(List<Integer> numList) {
        this.numList = numList;
    }

    public Integer getNumIndex() {
        return numIndex;
    }

    public void setNumIndex(Integer numIndex) {
        this.numIndex = numIndex;
    }

    public Integer getIpIndex() {
        return ipIndex;
    }

    public void setIpIndex(Integer ipIndex) {
        this.ipIndex = ipIndex;
    }

    public Integer getStartYear() {
        return startYear;
    }

    public void setStartYear(Integer startYear) {
        this.startYear = startYear;
    }

    public Integer getEndYear() {
        return endYear;
    }

    public void setEndYear(Integer endYear) {
        this.endYear = endYear;
    }

    public String getStartTimeStr() {
        return startTimeStr;
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public String getEndTimeStr() {
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }

    public String getSponsorPhone() {
        return sponsorPhone;
    }

    public void setSponsorPhone(String sponsorPhone) {
        this.sponsorPhone = sponsorPhone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgIntroduce() {
        return imgIntroduce;
    }

    public void setImgIntroduce(String imgIntroduce) {
        this.imgIntroduce = imgIntroduce;
    }
}
