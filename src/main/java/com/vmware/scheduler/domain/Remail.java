package com.vmware.scheduler.domain;

import org.springframework.data.annotation.Id;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.Properties;

/**
 * Created by ppushkar on 10/27/2015.
 */

public class Remail {

    @Id
    public String id;
    Map<String,String> payload;

    public Remail() {}
    public Remail(Map<String,String> payload)
    {
        this.payload=(Map) payload;
    }
    public String getId () { return id;  }
    public void setId (String id) { this.id=id;  }
    public Map getPayload(){ return payload;}
    public void setPayload(Map payload){ this.payload=payload;}


    public void execute()  {

        String to = payload.get("to");
        final String username = "fritz@vcac-mail.eng.vmware.com";
        final String password = "password";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "vcac-mail.eng.vmware.com");
        props.put("mail.smtp.port","25");

        javax.mail.Session session = javax.mail.Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("fritz@vcac-mail.eng.vmware.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            message.setSubject(payload.get("subject").toString());
            message.setText(payload.get("body").toString());
            Transport.send(message);
            System.out.println("Done");
        }

        catch (MessagingException e)
        {
            System.out.println(e);
        }
        }
    }
