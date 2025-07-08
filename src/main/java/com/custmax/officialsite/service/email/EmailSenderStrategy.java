package com.custmax.officialsite.service.email;

public interface EmailSenderStrategy {
    void sendResetPasswordEmail(String to, String resetUrl);
}
