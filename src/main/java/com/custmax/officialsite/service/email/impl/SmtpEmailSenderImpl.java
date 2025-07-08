package com.custmax.officialsite.service.email.impl;

import cn.hutool.core.io.resource.ClassPathResource;
import com.custmax.officialsite.service.email.EmailSenderStrategy;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * SMTP email sending strategy implementation
 *
 * @author Vincent
 */
@Component("smtp")
public class SmtpEmailSenderImpl implements EmailSenderStrategy {
    @Autowired
    private JavaMailSender mailSender;

    private void sendEmailWithTemplate(String to, String subject, String templateName, Map<String, String> params) {
        try {
            ClassPathResource resource = new ClassPathResource(templateName);
            String html = Files.readString(resource.getFile().toPath(), StandardCharsets.UTF_8);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                html = html.replace("{{" + entry.getKey() + "}}", entry.getValue());
            }

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);

            mailSender.send(message);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read email template", e);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
    @Override
    public void sendResetPasswordEmail(String to, String resetUrl) {
        Map<String, String> params = new HashMap<>();
        params.put("resetUrl", resetUrl);
        sendEmailWithTemplate(to, "Password Reset", "reset_password.html", params);
    }
}
