package com.custmax.officialsite.controller;

import com.custmax.officialsite.entity.CustomUserDetails;
import com.custmax.officialsite.entity.User;
import com.custmax.officialsite.service.UserService;
import com.custmax.officialsite.service.WebSiteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private WebSiteService webSiteService;

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
    @GetMapping("/me/domains")
    public List<String> getUserDomains() {
        // 假设 UserService 有方法获取用户域名
        return userService.getCurrentUserDomains();
    }

    @GetMapping("/me/subscription")
    public List<SubscriptionDTO> getUserSubscriptions() {
        return userService.getCurrentUserSubscriptions();
    }
    // List User Websites
    @GetMapping("/users/me/websites")
    public ResponseEntity<?> listUserWebsites(@AuthenticationPrincipal CustomUserDetails user) {
        List<?> websites = webSiteService.listUserWebsites(user.getUserId());
        return ResponseEntity.ok(websites);
    }

}