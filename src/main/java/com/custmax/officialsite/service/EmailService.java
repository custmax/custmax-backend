package com.custmax.officialsite.service;

public interface EmailService {
    void sendResetPassword(String to, String resetUrl);
}
