package com.notes.notes_app.utils;


import com.notes.notes_app.exceptions.CustomException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@PropertySource("classpath:props.properties")
@Service
public class EmailUtils {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${email.from}")
    private String from;

    @Value("${email.subject}")
    private String subject;

    @Value("${email.text}")
    private String emailMessage;

    public void sendBasicEmail(String to, String validationLink, String username) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            emailMessage = emailMessage.replace("{USERNAME}", username);
            emailMessage = emailMessage.replace("{VALIDATION_LINK}", validationLink);

            helper.setTo(to);
            helper.setFrom(from);
            helper.setText(emailMessage, true);
            helper.setSubject(subject);
            mailSender.send(message);
            System.out.println("Email sent successfully");
        } catch (MessagingException ex) {
            throw new CustomException(ex.getMessage() + " Email failed", "Email_FAILED");
        }

    }
}
