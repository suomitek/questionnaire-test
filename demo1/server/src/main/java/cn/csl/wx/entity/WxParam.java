package cn.csl.wx.entity;

import java.util.Date;

public class WxParam {
    private Long id;

    private Integer state;

    private Date createTime;

    private Integer projectType;

    private String appSecret;

    private String appId;

    private String mchId;

    private String accessToken;

    private String mchKey;

    private String keyPath;

    private String mpAppId;

    private String mpAppSecret;

    private String mpMchId;

    private String mpMchKey;

    private String mpAccessToken;

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

    public Integer getProjectType() {
        return projectType;
    }

    public void setProjectType(Integer projectType) {
        this.projectType = projectType;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret == null ? null : appSecret.trim();
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId == null ? null : mchId.trim();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken == null ? null : accessToken.trim();
    }

    public String getMchKey() {
        return mchKey;
    }

    public void setMchKey(String mchKey) {
        this.mchKey = mchKey == null ? null : mchKey.trim();
    }

    public String getKeyPath() {
        return keyPath;
    }

    public void setKeyPath(String keyPath) {
        this.keyPath = keyPath == null ? null : keyPath.trim();
    }

    public String getMpAppId() {
        return mpAppId;
    }

    public void setMpAppId(String mpAppId) {
        this.mpAppId = mpAppId == null ? null : mpAppId.trim();
    }

    public String getMpAppSecret() {
        return mpAppSecret;
    }

    public void setMpAppSecret(String mpAppSecret) {
        this.mpAppSecret = mpAppSecret == null ? null : mpAppSecret.trim();
    }

    public String getMpMchId() {
        return mpMchId;
    }

    public void setMpMchId(String mpMchId) {
        this.mpMchId = mpMchId == null ? null : mpMchId.trim();
    }

    public String getMpMchKey() {
        return mpMchKey;
    }

    public void setMpMchKey(String mpMchKey) {
        this.mpMchKey = mpMchKey == null ? null : mpMchKey.trim();
    }

    public String getMpAccessToken() {
        return mpAccessToken;
    }

    public void setMpAccessToken(String mpAccessToken) {
        this.mpAccessToken = mpAccessToken == null ? null : mpAccessToken.trim();
    }

    @Override
    public boolean equals(Object obj) {
        boolean flag = true;
        WxParam wxParam = (WxParam) obj;
        if(this.projectType==1||this.projectType==3){
            if(this.getAppId().equals(wxParam.getAppId())||this.getAppSecret().equals(wxParam.getAppSecret())){
                return flag;
            }
            if(this.getMchId().equals(wxParam.getMchId())||this.getMchKey().equals(wxParam.getMchKey())){
                return flag;
            }
        }
        if(this.projectType==2||this.projectType==3){
            if(this.getMpAppId().equals(wxParam.getMpAppId())||this.getMpAppSecret().equals(wxParam.getMpAppSecret())){
                return flag;
            }
            if(this.getMpMchId().equals(wxParam.getMpMchId())||this.getMpMchKey().equals(wxParam.getMpMchKey())){
                return flag;
            }
        }
        return false;
    }
}