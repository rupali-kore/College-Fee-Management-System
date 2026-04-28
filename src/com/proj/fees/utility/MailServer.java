package com.proj.fees.utility;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import com.proj.fees.properties.PropertyReader;

public class MailServer {
    
    private PropertyReader prop;

    public MailServer() {
        prop = new PropertyReader();
    }

    public void sendMail(String recipient, String subjectLine, String messageContent) throws MessagingException {
        
        System.out.println("📩 Preparing to send mail to: " + recipient);
        
        // properties फाईलमधून ईमेल आणि अप्प-पासवर्ड वाचणे
        final String myAccEmail = prop.getProperty("ADMINEMAILID");
        final String myAccPassword = prop.getProperty("ADMINEMAILPASSWORD");

        // SMTP सर्व्हर कॉन्फिगरेशन
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // TLS सुरक्षित कनेक्शनसाठी
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587"); // Google कडून शिफारस केलेला पोर्ट

        // ईमेल सेशन तयार करणे
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccEmail, myAccPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(subjectLine);
            message.setText(messageContent);

            // ईमेल पाठवणे
            Transport.send(message);
            System.out.println("✅ Message sent successfully!");

        } catch (MessagingException e) {
            System.err.println("❌ Failed to send mail: " + e.getMessage());
            throw e; // Error पुन्हा थ्रो करा जेणेकरून UI मध्ये मेसेज दाखवता येईल
        }
    }
}