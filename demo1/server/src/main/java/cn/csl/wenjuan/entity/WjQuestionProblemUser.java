package cn.csl.wenjuan.entity;

import java.util.Date;

public class WjQuestionProblemUser {
    private Long id;

    private Integer state;

    private Date createTime;

    private Long queProId;

    private Long wxUserId;

    private Integer kind;

    private String choiceIds;

    private String vacancy;

    private String content;

    private String filePath;

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

    public Long getQueProId() {
        return queProId;
    }

    public void setQueProId(Long queProId) {
        this.queProId = queProId;
    }

    public Long getWxUserId() {
        return wxUserId;
    }

    public void setWxUserId(Long wxUserId) {
        this.wxUserId = wxUserId;
    }

    public Integer getKind() {
        return kind;
    }

    public void setKind(Integer kind) {
        this.kind = kind;
    }

    public String getChoiceIds() {
        return choiceIds;
    }

    public void setChoiceIds(String choiceIds) {
        this.choiceIds = choiceIds == null ? null : choiceIds.trim();
    }

    public String getVacancy() {
        return vacancy;
    }

    public void setVacancy(String vacancy) {
        this.vacancy = vacancy == null ? null : vacancy.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath == null ? null : filePath.trim();
    }
}