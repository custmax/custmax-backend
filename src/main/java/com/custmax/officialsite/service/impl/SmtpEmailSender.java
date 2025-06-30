package com.custmax.officialsite.service.impl;

import cn.hutool.core.io.resource.ClassPathResource;
import com.custmax.officialsite.service.EmailSenderStrategy;
import jakarta.mail.MessagingException;
import org.springframework.mail.javamail.JavaMailSender;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class SmtpEmailSender implements EmailSenderStrategy {
    private final JavaMailSender mailSender;

    public SmtpEmailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendResetPasswordEmail(String to, String resetUrl) {
        try {
            // read HTML template from resources
            ClassPathResource resource = new ClassPathResource("reset_password.html");
            String html = Files.readString(resource.getFile().toPath(), StandardCharsets.UTF_8);
            html = html.replace("{{resetUrl}}", resetUrl);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("hello@custmax.com");
            helper.setTo(to);
            helper.setSubject("Password Reset");
            helper.setText(html, true);

            mailSender.send(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }


    }
}
