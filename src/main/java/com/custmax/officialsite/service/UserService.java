package com.custmax.officialsite.service;

import com.custmax.officialsite.dto.subscription.SubscriptionResponse;
import com.custmax.officialsite.dto.user.LoginResponse;
import com.custmax.officialsite.dto.website.WebsiteDTO;
import com.custmax.officialsite.entity.user.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {
    User register(String email, String password, String username, String inviteCode);
    LoginResponse login(String email, String password);
    User getCurrentUser();
    void resetPassword(String token, String newPassword);
    void sendResetPasswordEmail(String email);
    List<WebsiteDTO> getCurrentUserWebsites();
    List<String> getCurrentUserDomains();
    List<SubscriptionResponse> getCurrentUserSubscriptions();
}