package cn.csl.wenjuan.entity;

import java.util.Date;
import java.util.List;

public class WjProblem {
    private Long id;

    private Date createTime;

    private Integer state;

    private String name;

    private Long questionId;

    private Integer orderNum;

    private String qType;

    private String cover;

    private Integer must;

    private Integer choicesNunber;

    private Integer answerNumber;

    private Integer lineNumber;

    private String checkType;

    private Long checkId;

    private Integer maxFen;
    private Integer fraction;

    private Integer score;

    private Integer genre;

    private Long wxUserId;

    private String reg;
    private String msg;
    private String answerId;
    private Integer examineFlag;

    private List<WjChoice> choices;

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public Integer getFraction() {
        return fraction;
    }

    public void setFraction(Integer fraction) {
        this.fraction = fraction;
    }

    public String getMsg() {
        return msg;
    }

    public String getReg() {
        return reg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }

    public List<WjChoice> getChoices() {
        return choices;
    }

    public void setChoices(List<WjChoice> choices) {
        this.choices = choices;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getqType() {
        return qType;
    }

    public void setqType(String qType) {
        this.qType = qType == null ? null : qType.trim();
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover == null ? null : cover.trim();
    }

    public Integer getMust() {
        return must;
    }

    public void setMust(Integer must) {
        this.must = must;
    }

    public Integer getChoicesNunber() {
        return choicesNunber;
    }

    public void setChoicesNunber(Integer choicesNunber) {
        this.choicesNunber = choicesNunber;
    }

    public Integer getAnswerNumber() {
        return answerNumber;
    }

    public void setAnswerNumber(Integer answerNumber) {
        this.answerNumber = answerNumber;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType == null ? null : checkType.trim();
    }

    public Long getCheckId() {
        return checkId;
    }

    public void setCheckId(Long checkId) {
        this.checkId = checkId;
    }

    public Integer getMaxFen() {
        return maxFen;
    }

    public void setMaxFen(Integer maxFen) {
        this.maxFen = maxFen;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getGenre() {
        return genre;
    }

    public void setGenre(Integer genre) {
        this.genre = genre;
    }

    public Long getWxUserId() {
        return wxUserId;
    }

    public void setWxUserId(Long wxUserId) {
        this.wxUserId = wxUserId;
    }

    public Integer getExamineFlag() {
        return examineFlag;
    }

    public void setExamineFlag(Integer examineFlag) {
        this.examineFlag = examineFlag;
    }
}