package com.custmax.officialsite.service.impl;

import com.custmax.officialsite.service.EmailSenderStrategy;
import com.custmax.officialsite.service.EmailService;

public class EmailServiceImpl implements EmailService {
    private final EmailSenderStrategy emailSender;

    public EmailServiceImpl(EmailSenderStrategy emailSender) {
        this.emailSender = emailSender;
    }
    @Override
    public void sendResetPassword(String to, String resetUrl) {
        emailSender.sendResetPasswordEmail(to, resetUrl);
    }
}
