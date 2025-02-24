package com.file_hider.services;

import java.util.Random;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class OTPService {
    private final Random random = new Random();

    public String generateOTP() {
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public void sendOTPEmail(String toEmail, String otp) throws MessagingException {
        try {
            String fromEmail = "barotshivam864@gmail.com";
            String password = "vpvg emru vchg rgtx";
    
            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");
    
            Session session = Session.getInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            });
    
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("OTP for authentication");
            message.setText("Your OTP code is: " + otp);
    
            Transport.send(message);
    
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
