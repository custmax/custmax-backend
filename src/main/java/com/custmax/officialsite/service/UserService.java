package com.custmax.officialsite.service;

import com.custmax.officialsite.dto.LoginResponse;
import com.custmax.officialsite.entity.User;

public interface UserService {
    User register(String email, String password, String username, String inviteCode);
    LoginResponse login(String email, String password);
    User getCurrentUser();
    void resetPassword(String token, String newPassword);
    void sendResetPasswordEmail(String email);
}