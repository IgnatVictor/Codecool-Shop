package com.codecool.shop.util;

import com.codecool.shop.config.ConnectionManager;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;


public class MailSender {
    public static void sendConfirmation(String toEmail, String name) throws IOException {
        Properties senderProperties = ConnectionManager.getProperties("mail.properties");
        String fromEmail = senderProperties.getProperty("address");
        String password = senderProperties.getProperty("password");

        System.out.println("SSLEmail start");
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };

        Session session = Session.getDefaultInstance(properties, auth);
        System.out.println("Session created");
        String messageBody = "Dear " + name + ",\n\n" + "Thank you for your registration! \uD83D\uDC36 \uD83D\uDC31 \uD83D\uDC30 \n\n CC Shop Team";
        send(session, toEmail, messageBody);
    }

    private static void send(Session session, String toEmail, String body){
        try {
            MimeMessage msg = new MimeMessage(session);

            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress("info.ccshop.petsupplies@gmail.com", "CC Shop info"));

            msg.setReplyTo(InternetAddress.parse("info.ccshop.petsupplies@gmail.com", false));

            msg.setSubject("Successful registration", "UTF-8");

            msg.setText(body, "UTF-8");

            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            System.out.println("Message is ready");
            Transport.send(msg);

            System.out.println("Mail sent successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
