package com.notes.notes_app.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@PropertySource("classpath:props.properties")
@Service
public class EmailUtils {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${email.from}")
    private String from;

    @Value("${email.subject}")
    private String subject;

    @Value("${email.text}")
    private String body;

    public void sendBasicEmail(String to, String message) {
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setTo(to);
        smm.setFrom(from);
        smm.setText(body+" Link : "+ message);
        smm.setSubject(subject);
        javaMailSender.send(smm);
        System.out.println("Email sent successfully");
    }
}
