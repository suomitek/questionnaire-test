package cn.csl.wenjuan.front.vo;

import cn.csl.basics.util.DatetimeUtils;
import cn.csl.wenjuan.entity.WjVote;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WjVoteVo {
    private Long id;
    private Long commentNum;//总评论数
    private Integer state;
    private Date createTime;
    private String createTimeStr;
    private String name;
    private String cover;
    private String introduce;
    private String themeColor;
    private String imgIntroduce;
    private Integer repeatFlag;
    private Integer frequency;
    private Integer restrictFlag;
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
    private String startTimeStr;
    private String endTimeStr;
    private Long startTimeStamp;
    private Long endTimeStamp;
    private Integer voteType;
    private Integer unusual;
    private String unusualEplain;
    private String reportEplain;
    private Long rWxUserId;
    private Long wxUserId;
    private Integer candidateNum;
    private Integer candidateIndex;
    private Integer supNum;
    private Integer suspend;
    private String voteDataStr;
    private String voteCode;
    private String schedule;//进度
    private Integer enlist;//显示报名 0 不显示 1 显示 添加 2 管理员  管理 候选人
    private List<WjCandidateVo> candidateVos;//候选项列表
    private List<WjCandidateVo> candidateVo2s;//排行列表
    private List<String> imgIntroduces;//
    private List<String> rewarImgs;//
    private List<String> covers;//


    public WjVoteVo(WjVote vote,String schedule,List<String> covers){//列表版本初始化  只需要 封面图 shijian
        try {
            BeanUtils.copyProperties(this, vote);
        }catch (Exception e){
            e.printStackTrace();
        }
        this.voteDataStr = "";
        this.createTimeStr = DatetimeUtils.dateToString(vote.getCreateTime());
        this.startTimeStr = DatetimeUtils.dateToString(vote.getStartTime());
        this.endTimeStr = DatetimeUtils.dateToString(vote.getEndTime());
        this.covers = covers;//
        this.schedule = schedule;//进度
    }
    public WjVoteVo(WjVote vote,String schedule,List<String> covers,List<String> imgIntroduces
            ,List<String> rewarImgs,List<WjCandidateVo> candidateVos
            ,int enlist){//详细版本初始化
        try {
            BeanUtils.copyProperties(this, vote);
        }catch (Exception e){
            e.printStackTrace();
        }


        this.schedule = schedule;//进度

        this.createTimeStr = DatetimeUtils.dateToString(vote.getCreateTime());

        //显示报名 0 不显示 1 显示 添加 2 管理员  管理 候选人
        this.enlist = enlist;
        // 封面
        this.covers = covers;
        //介绍 图列表
        this.imgIntroduces = imgIntroduces;
        //奖里说明 图列表
        this.rewarImgs = rewarImgs;

        // 时间
        this.createTimeStr = DatetimeUtils.dateToString(vote.getCreateTime());
        this.startTimeStr = DatetimeUtils.dateToString(vote.getStartTime());
        this.endTimeStr = DatetimeUtils.dateToString(vote.getEndTime());
        this.startTimeStamp = vote.getStartTime().getTime();
        this.endTimeStamp = vote.getEndTime().getTime();

        //候选项
        this.candidateVos = candidateVos;
        //排行榜项
        this.candidateVo2s = this.initCandidateVo2s(candidateVos);
    }

    private List<WjCandidateVo> initCandidateVo2s(List<WjCandidateVo> candidateVo0s){
        List<WjCandidateVo> candidateVo2s = new ArrayList<>();
        List<WjCandidateVo> candidateVos = new ArrayList<>(candidateVo0s);
        while (candidateVos.size()>0){
            int max = getMaxIndex(candidateVos);
            candidateVo2s.add(candidateVos.get(max));
            candidateVos.remove(max);
        }
        return candidateVo2s;
    }
    private int getMaxIndex(List<WjCandidateVo> candidateVos){
        int max = 0;
        for (int x=0;x<candidateVos.size();x++){
            if (candidateVos.get(max).getSupNum()<candidateVos.get(x).getSupNum()){
                max = x;
            }
        }
        return max;
    }

    public Integer getEnlist() {
        return enlist;
    }

    public void setEnlist(Integer enlist) {
        this.enlist = enlist;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public Long getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Long commentNum) {
        this.commentNum = commentNum;
    }

    public Integer getSuspend() {
        return suspend;
    }

    public void setSuspend(Integer suspend) {
        this.suspend = suspend;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public List<String> getImgIntroduces() {
        return imgIntroduces;
    }

    public void setImgIntroduces(List<String> imgIntroduces) {
        this.imgIntroduces = imgIntroduces;
    }

    public List<String> getRewarImgs() {
        return rewarImgs;
    }

    public void setRewarImgs(List<String> rewarImgs) {
        this.rewarImgs = rewarImgs;
    }

    public List<String> getCovers() {
        return covers;
    }

    public void setCovers(List<String> covers) {
        this.covers = covers;
    }

    public List<WjCandidateVo> getCandidateVo2s() {
        return candidateVo2s;
    }

    public void setCandidateVo2s(List<WjCandidateVo> candidateVo2s) {
        this.candidateVo2s = candidateVo2s;
    }

    public Long getEndTimeStamp() {
        return endTimeStamp;
    }

    public void setEndTimeStamp(Long endTimeStamp) {
        this.endTimeStamp = endTimeStamp;
    }

    public Long getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setStartTimeStamp(Long startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
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

    public String getThemeColor() {
        return themeColor;
    }

    public void setThemeColor(String themeColor) {
        this.themeColor = themeColor;
    }

    public String getImgIntroduce() {
        return imgIntroduce;
    }

    public void setImgIntroduce(String imgIntroduce) {
        this.imgIntroduce = imgIntroduce;
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
        this.sponsorName = sponsorName;
    }

    public String getSponsorPhone() {
        return sponsorPhone;
    }

    public void setSponsorPhone(String sponsorPhone) {
        this.sponsorPhone = sponsorPhone;
    }

    public Integer getReward() {
        return reward;
    }

    public void setReward(Integer reward) {
        this.reward = reward;
    }

    public String getRewardDesc() {
        return rewardDesc;
    }

    public void setRewardDesc(String rewardDesc) {
        this.rewardDesc = rewardDesc;
    }

    public String getRewarImg() {
        return rewarImg;
    }

    public void setRewarImg(String rewarImg) {
        this.rewarImg = rewarImg;
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

    public String getVoteDataStr() {
        return voteDataStr;
    }

    public void setVoteDataStr(String voteDataStr) {
        this.voteDataStr = voteDataStr;
    }

    public String getVoteCode() {
        return voteCode;
    }

    public void setVoteCode(String voteCode) {
        this.voteCode = voteCode;
    }

    public List<WjCandidateVo> getCandidateVos() {
        return candidateVos;
    }

    public void setCandidateVos(List<WjCandidateVo> candidateVos) {
        this.candidateVos = candidateVos;
    }
}
