package com.bash.Unitrack.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {
    @Value("${app.email}")
    private String fromEmail;

    private final JavaMailSender mailSender;
    private final EmailTemplateService templateService;

    public EmailService(JavaMailSender mailSender, EmailTemplateService templateService) {
        this.mailSender = mailSender;
        this.templateService = templateService;
    }

    public void verificationEmail(String name, String to, String resetLink) throws IOException, MessagingException {
        String content = templateService.getPasswordResetEmailContent(name, resetLink, "15 minutes");
        send(to, "Email verification", content);
    }

    public void passwordResetEmail(String name, String to, String resetLink) throws IOException, MessagingException {
        String content = templateService.getPasswordResetEmailContent(name, resetLink, "15 minutes");
        send(to, "Password reset", content);
    }

    public void send(String to, String subject, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content,true);
        mailSender.send(message);
    }
}
