package com.custmax.officialsite.service.email;

public interface EmailService {
    void sendResetPassword(String strategy, String to, String resetUrl);
}
