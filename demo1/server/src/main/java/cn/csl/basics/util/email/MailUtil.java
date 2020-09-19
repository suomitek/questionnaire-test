package cn.csl.basics.util.email;

import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class MailUtil
{
    public Boolean send(Mail mail)
    {
        HtmlEmail email = new HtmlEmail();
        try
        {
            email.setHostName(mail.getHost());

            email.setCharset("UTF-8");

            email.setFrom(mail.getSender(), mail.getName());

            email.setAuthentication(mail.getUsername(), mail.getPassword());

            setTo(email, mail);

            setCc(email, mail);

            setBcc(email, mail);

            email.setSubject(mail.getSubject());

            email.setHtmlMsg(mail.getMessage());

            email.send();
            return Boolean.valueOf(true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return Boolean.valueOf(false);
    }

    private void setTo(HtmlEmail email, Mail mail)
            throws EmailException
    {
        if (StringUtils.isNotEmpty(mail.getReceiver())) {
            if (StringUtils.isNotEmpty(mail.getReceiverName())) {
                email.addTo(mail.getReceiver(), mail.getReceiverName());
            } else {
                email.addTo(mail.getReceiver());
            }
        }
        if (mail.getTo() != null) {
            for (Map.Entry<String, String> entry : mail.getTo().entrySet()) {
                if (StringUtils.isNotEmpty((String)entry.getValue())) {
                    email.addTo((String)entry.getKey(), (String)entry.getValue());
                } else {
                    email.addTo((String)entry.getKey());
                }
            }
        }
    }

    private void setCc(HtmlEmail email, Mail mail)
            throws EmailException
    {
        if (mail.getCc() != null) {
            for (Map.Entry<String, String> entry : mail.getCc().entrySet()) {
                if (StringUtils.isNotEmpty((String)entry.getValue())) {
                    email.addCc((String)entry.getKey(), (String)entry.getValue());
                } else {
                    email.addCc((String)entry.getKey());
                }
            }
        }
    }

    private void setBcc(HtmlEmail email, Mail mail)
            throws EmailException
    {
        if (mail.getBcc() != null) {
            for (Map.Entry<String, String> entry : mail.getBcc().entrySet()) {
                if (StringUtils.isNotEmpty((String)entry.getValue())) {
                    email.addBcc((String)entry.getKey(), (String)entry.getValue());
                } else {
                    email.addBcc((String)entry.getKey());
                }
            }
        }
    }
}
