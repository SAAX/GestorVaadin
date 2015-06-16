package com.saax.gestorweb.util;

import com.saax.gestorweb.GestorMDI;
import com.vaadin.ui.UI;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender {

    private final Session session;

    public MailSender() {

        final String mailUserName = ((GestorMDI) UI.getCurrent()).getApplication().getProperty("mail.username");
        final String mailPasswd = ((GestorMDI) UI.getCurrent()).getApplication().getProperty("mail.passwd");

        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");

        
        
        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailUserName, mailPasswd);
                    }
                });
        session.setDebug(true);

    }
    
   

    public void sendMail(String destinationAddresses, String subject, String htmlBoby) {
        try {

            final String mailUserName = ((GestorMDI) UI.getCurrent()).getApplication().getProperty("mail.username");
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailUserName)); 

            Address[] toUser = InternetAddress.parse(destinationAddresses);

            message.setRecipients(Message.RecipientType.TO, toUser);
            message.setSubject(subject);
            message.setContent(htmlBoby, "text/html");
            
            Transport.send(message);


        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
