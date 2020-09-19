package cn.csl.basics.util.email;

public class EmailUtil
{
    public static boolean sendMail(String host, String sender, String name, String password, String receiver, String receiverName, String subject, String html)
    {
        Mail mail = new Mail();
        mail.setHost(host);
        mail.setSender(sender);
        mail.setName(name);
        mail.setUsername(sender);
        mail.setPassword(password);
        mail.setReceiver(receiver);
        mail.setReceiverName(receiverName);
        mail.setSubject(subject);
        mail.setMessage(html);
        return new MailUtil().send(mail);
    }
}
