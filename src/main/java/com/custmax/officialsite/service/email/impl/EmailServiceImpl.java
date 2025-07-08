package com.custmax.officialsite.service.email.impl;

import com.custmax.officialsite.service.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * email
 *
 *
 * @author Vincent
 */
@Service
public class EmailServiceImpl implements EmailService {
    private final EmailSenderStrategyFactory emailSenderContext;

    @Autowired
    public EmailServiceImpl(EmailSenderStrategyFactory emailSenderContext) {
        this.emailSenderContext = emailSenderContext;
    }

    @Override
    public void sendResetPassword(String strategy, String to, String resetUrl) {
        emailSenderContext.sendResetPasswordEmail(strategy, to, resetUrl);
    }

}
