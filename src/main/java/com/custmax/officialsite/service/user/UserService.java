package com.custmax.officialsite.service.user;

import com.custmax.officialsite.dto.user.LoginResponse;
import com.custmax.officialsite.dto.user.UserRegisterRequest;
import com.custmax.officialsite.dto.user.UserRegisterResponse;
import com.custmax.officialsite.entity.user.User;


public interface UserService {
    UserRegisterResponse register(UserRegisterRequest request);
    LoginResponse login(String email, String password);
    User getCurrentUser();
    void resetPassword(String token, String newPassword);
    void sendResetPasswordEmail(String email);
}