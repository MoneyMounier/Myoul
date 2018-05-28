package com.server.myoul;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailServer {

    // Sender's email ID needs to be mentioned
    private static final String user = "no-reply@myoul.com";
    private static final String pass = "Wasr13!!";


    public static void sendEmail(String to, String subject, String content) {


        // Get system properties
        Properties props = System.getProperties();

        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Authorized the Session object.
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
        });

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(user));
            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            // Set Subject: header field
            message.setSubject(subject);
            // Now set the actual message
            message.setText(content);
            //save message
            message.saveChanges();

            // Send message
            Transport.send(message);

            /*
            //Get transport for session
            Transport transport = session.getTransport("smtp");
            //Connect
            transport.connect(host, from, to);
            //repeat if necessary
            transport.sendMessage(message, message.getAllRecipients());
            //Done, close the connection
            transport.close();
            */

            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public static void sendTest(String to){
        sendEmail(to, "test", "test");
    }
}
