package cn.csl.basics.util.email;

import java.io.Serializable;
import java.util.Map;

public class Mail
        implements Serializable
{
    private static final long serialVersionUID = -6390720891150157552L;
    public static final String ENCODEING = "UTF-8";
    private String host;
    private String sender;
    private String name;
    private String username;
    private String password;
    private String receiver;
    private String receiverName;
    private Map<String, String> to;
    private Map<String, String> cc;
    private Map<String, String> bcc;
    private String subject;
    private String message;

    public String getHost()
    {
        return this.host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public String getSender()
    {
        return this.sender;
    }

    public void setSender(String sender)
    {
        this.sender = sender;
    }

    public String getReceiver()
    {
        return this.receiver;
    }

    public void setReceiver(String receiver)
    {
        this.receiver = receiver;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getUsername()
    {
        return this.username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return this.password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getSubject()
    {
        return this.subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public String getMessage()
    {
        return this.message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getReceiverName()
    {
        return this.receiverName;
    }

    public void setReceiverName(String receiverName)
    {
        this.receiverName = receiverName;
    }

    public Map<String, String> getTo()
    {
        return this.to;
    }

    public void setTo(Map<String, String> to)
    {
        this.to = to;
    }

    public Map<String, String> getCc()
    {
        return this.cc;
    }

    public void setCc(Map<String, String> cc)
    {
        this.cc = cc;
    }

    public Map<String, String> getBcc()
    {
        return this.bcc;
    }

    public void setBcc(Map<String, String> bcc)
    {
        this.bcc = bcc;
    }
}
