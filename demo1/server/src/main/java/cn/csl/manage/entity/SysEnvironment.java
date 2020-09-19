package cn.csl.manage.entity;

public class SysEnvironment {
    private Long id;
    private String name;
    private String emailHost;
    private String emailSender;
    private String emailPassword;
    private String introduceVideoUrl;

    public String getIntroduceVideoUrl() {
        return introduceVideoUrl;
    }

    public void setIntroduceVideoUrl(String introduceVideoUrl) {
        this.introduceVideoUrl = introduceVideoUrl;
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
        this.name = name;
    }

    public String getEmailHost() {
        return emailHost;
    }

    public void setEmailHost(String emailHost) {
        this.emailHost = emailHost;
    }

    public String getEmailSender() {
        return emailSender;
    }

    public void setEmailSender(String emailSender) {
        this.emailSender = emailSender;
    }

    public String getEmailPassword() {
        return emailPassword;
    }

    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }
}
