package cn.csl.manage.entity;

import cn.csl.basics.util.annotation.check.CheckAnnotation;
import cn.csl.basics.util.annotation.search.SearchAnnotation;
import cn.csl.basics.util.annotation.unique.UniqueAnnotation;

import java.util.Date;

public class SysUser {
    private Long id;
    @SearchAnnotation(searchMsg = "用户名")
    @CheckAnnotation(type = "required",attribute="用户名")
    private String name;
    @UniqueAnnotation(uniqueMsg = "用户昵称已被使用")
    @SearchAnnotation(searchMsg = "用户昵称")
    @CheckAnnotation(type = "required",attribute="用户昵称")
    private String nickName;
    @CheckAnnotation(type = "password",attribute="密码")
    private String password;

    private Integer useFlag;

    private Integer state;
//    @CheckAnnotation(type = "url",attribute="网址")
    private String portraitUrl;

    private Date createTime;
    @UniqueAnnotation(uniqueMsg = "用户电话已被使用")
    @SearchAnnotation(searchMsg = "用户电话")
    @CheckAnnotation(type = "isMobile",attribute="用户电话")
    private String telNumber;
    @UniqueAnnotation(uniqueMsg = "用户邮箱已被使用")
    @SearchAnnotation(searchMsg = "用户邮箱")
    @CheckAnnotation(type = "email",attribute="用户邮箱")
    private String mailbox;

    private Integer userType;

    private Long adminId;

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public void setUseFlag(Integer useFlag) {
        this.useFlag = useFlag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
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

    public void setCreateTime(Date creatTime) {
        this.createTime = creatTime;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber == null ? null : telNumber.trim();
    }

    public String getMailbox() {
        return mailbox;
    }

    public void setMailbox(String mailbox) {
        this.mailbox = mailbox == null ? null : mailbox.trim();
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getUseFlag() {
        return useFlag;
    }

    public void setUse(Integer useFlag) {
        this.useFlag = useFlag;
    }

    public String getPortraitUrl() {
        return portraitUrl;
    }

    public void setPortraitUrl(String portraitUrl) {
        this.portraitUrl = portraitUrl;
    }
}