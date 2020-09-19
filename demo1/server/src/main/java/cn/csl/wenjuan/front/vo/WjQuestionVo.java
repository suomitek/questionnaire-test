package cn.csl.wenjuan.front.vo;

import cn.csl.basics.util.DatetimeUtils;
import cn.csl.wenjuan.entity.WjQuestion;
import cn.csl.wenjuan.entity.WjVote;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Date;
import java.util.List;

public class WjQuestionVo {
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
    private String questionDataStr;
    private String problemsStr;


    private String createTimeStr;
    private String startTimeStr;
    private String endTimeStr;
    private String schedule;
    private int flag;
    private int fraction;
    private int answer;//1之前回答过
    public WjQuestionVo(WjQuestion question){/////////列表
        JSONObject queData = JSONObject.parseObject(question.getQuestionDataStr());
        this.cover = queData.getString("cover");
        this.examineFlag = question.getExamineFlag();
        this.problemsStr = question.getCproblemsStr();/////////////列表的时候用带答案的
        this.questionDataStr=question.getQuestionDataStr();
        this.suspend = question.getSuspend();
        this.id = question.getId();
        this.name = question.getName();
        this.answerNumber = question.getAnswerNumber();//答卷人数
        this.state = question.getState();
        this.createTimeStr = DatetimeUtils.dateToString(question.getCreateTime());
        this.startTimeStr = DatetimeUtils.dateToString(question.getStartTime());
        this.endTimeStr = DatetimeUtils.dateToString(question.getEndTime());
        if (question.getSuspend()==0){
            this.flag = 0;//暂停
            this.schedule = "zanting";
        }else{
            if(new Date().before(question.getStartTime())){//weilkaishi
                this.flag = 1;
                this.schedule = "weikaishi";
            }else if(new Date().before(question.getEndTime())){//进行中
                this.flag = 2;
                this.schedule = "jinxingzhong";
            }else {
                this.flag = 3;//已经结束了
                this.schedule = "yijieshu";
            }
        }
    }
    public WjQuestionVo(WjQuestion question,String problemsStr,int answer,int fraction){//列表版本初始化  只需要 封面图
        try {
            BeanUtils.copyProperties(this, question);
        }catch (Exception e){
            e.printStackTrace();
        }
        this.sumScore = question.getSumScore();
        this.problemsStr = problemsStr;
        this.fraction = fraction;
        this.answer = answer;
        this.name = question.getName();
        this.suspend = question.getSuspend();
        this.overt = question.getOvert();//////////可修改是否公开
        this.createTimeStr = DatetimeUtils.dateToString(question.getCreateTime());
        this.startTimeStr = DatetimeUtils.dateToString(question.getStartTime());
        this.endTimeStr = DatetimeUtils.dateToString(question.getEndTime());
        if (question.getSuspend()==0){
            this.flag = 0;//暂停
        }else{
            if(new Date().before(question.getStartTime())){//weilkaishi
                this.flag = 1;
            }else if(new Date().before(question.getEndTime())){//进行中
                this.flag = 2;
            }else {
                this.flag = 3;//已经结束了
            }
        }
    }

    public int getFraction() {
        return fraction;
    }

    public void setFraction(int fraction) {
        this.fraction = fraction;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
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

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
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
        this.name = name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getImgIntroduce() {
        return imgIntroduce;
    }

    public void setImgIntroduce(String imgIntroduce) {
        this.imgIntroduce = imgIntroduce;
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
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
        this.sponsorName = sponsorName;
    }

    public String getSponsorPhone() {
        return sponsorPhone;
    }

    public void setSponsorPhone(String sponsorPhone) {
        this.sponsorPhone = sponsorPhone;
    }

    public String getVoteCode() {
        return voteCode;
    }

    public void setVoteCode(String voteCode) {
        this.voteCode = voteCode;
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
        this.unusualEplain = unusualEplain;
    }

    public String getReportEplain() {
        return reportEplain;
    }

    public void setReportEplain(String reportEplain) {
        this.reportEplain = reportEplain;
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

    public int getIpWxUserFrequency() {
        return ipWxUserFrequency;
    }

    public void setIpWxUserFrequency(int ipWxUserFrequency) {
        this.ipWxUserFrequency = ipWxUserFrequency;
    }

    public String getQuestionDataStr() {
        return questionDataStr;
    }

    public void setQuestionDataStr(String questionDataStr) {
        this.questionDataStr = questionDataStr;
    }

    public String getProblemsStr() {
        return problemsStr;
    }

    public void setProblemsStr(String problemsStr) {
        this.problemsStr = problemsStr;
    }
}
