package com.custmax.officialsite.controller;

import com.custmax.officialsite.dto.LoginRequest;
import com.custmax.officialsite.dto.LoginResponse;
import com.custmax.officialsite.dto.SubscriptionDTO;
import com.custmax.officialsite.dto.WebsiteDTO;
import com.custmax.officialsite.entity.User;
import com.custmax.officialsite.service.UserService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Resource
    private UserService userService;


    @PostMapping("/register")
    public User register(@RequestParam String email, @RequestParam String password,
                         @RequestParam String username, @RequestParam String inviteCode) {
        return userService.register(email, password, username, inviteCode);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest.getEmail(), loginRequest.getPassword());
    }

    @GetMapping("/me")
    public User me() {
        return userService.getCurrentUser();
    }
    // 发送重置邮件
    @PostMapping("/forgot-password")
    public void forgotPassword(@RequestParam String email) {
        userService.sendResetPasswordEmail(email);
    }

    // 重置密码
    @PostMapping("/reset-password")
    public void resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        userService.resetPassword(token, newPassword);
    }
}