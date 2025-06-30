package com.custmax.officialsite.service;

public interface EmailSenderStrategy {
    void sendResetPasswordEmail(String to, String resetUrl);
}
