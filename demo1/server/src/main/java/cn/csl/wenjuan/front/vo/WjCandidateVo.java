package cn.csl.wenjuan.front.vo;

import cn.csl.basics.util.DatetimeUtils;
import cn.csl.wenjuan.entity.WjCandidate;
import cn.csl.wx.entity.WxUser;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Date;
import java.util.List;

public class WjCandidateVo {
    private Long id;
    private Integer state;
    private Date createTime;
    private String createTimeStr;
    private Integer orderNum;
    private String cover;
    private String imgIntroduce;
    private String introduce;
    private String name;
    private Integer supNum;
    private Integer useFlag;
    private Long voteId;
    private Integer voteType;

    private Long wxUserId;
    private WxUser wxUser;//
    private List<String> imgIntroduces;//
    private List<String> covers;//

    public WjCandidateVo(){}
    public WjCandidateVo(WjCandidate wjCandidate,List<String> covers,List<String> imgIntroduces){
        try {
            BeanUtils.copyProperties(this, wjCandidate);
        }catch (Exception e){
            e.printStackTrace();
        }
        this.covers = covers;//
        // 时间
        this.createTimeStr = DatetimeUtils.dateToString(wjCandidate.getCreateTime());
        //图片介绍
        this.imgIntroduces = imgIntroduces;

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

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
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
        this.introduce = introduce;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<String> getImgIntroduces() {
        return imgIntroduces;
    }

    public void setImgIntroduces(List<String> imgIntroduces) {
        this.imgIntroduces = imgIntroduces;
    }

    public List<String> getCovers() {
        return covers;
    }

    public void setCovers(List<String> covers) {
        this.covers = covers;
    }
}
