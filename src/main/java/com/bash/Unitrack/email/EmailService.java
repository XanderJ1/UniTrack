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

    @Value("${app.baseUrl}")
    private String baseUrl;

    private final JavaMailSender mailSender;
    private final EmailTemplateService templateService;

    public EmailService(JavaMailSender mailSender, EmailTemplateService templateService) {
        this.mailSender = mailSender;
        this.templateService = templateService;
    }

    public void verifyEmail(String name, String to, String verificationToken) throws IOException, MessagingException {
        String verificationLink = baseUrl + "/auth/verify-email?token=" + verificationToken;
        String content = templateService.getVerificationEmailContent(name, verificationLink, "15 minutes");
        send(to, "Email verification", content);
    }

    public void passwordResetEmail(String name, String to, String resetToken) throws IOException, MessagingException {
        String resetLink = baseUrl + "/auth/password-reset?token=" + resetToken;
        String content = templateService.getPasswordResetEmailContent(name, resetLink, "15 minutes");
        send(to, "Password reset", content);
    }

    public void send(String to, String subject, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true, "UTF-8");
        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content,true);
        mailSender.send(message);
    }
}
