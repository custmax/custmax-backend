package com.custmax.officialsite.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.custmax.officialsite.dto.subscription.SubscriptionDTO;
import com.custmax.officialsite.dto.user.LoginResponse;
import com.custmax.officialsite.dto.website.WebsiteDTO;
import com.custmax.officialsite.entity.user.CustomUserDetails;
import com.custmax.officialsite.entity.user.User;
import com.custmax.officialsite.entity.invite.InviteCode;
import com.custmax.officialsite.mapper.UserMapper;
import com.custmax.officialsite.mapper.InviteCodeMapper;
import com.custmax.officialsite.service.UserService;
import com.custmax.officialsite.util.JwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private InviteCodeMapper inviteCodeMapper;


    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User register(String email, String password, String username, String inviteCode) {
        InviteCode code = inviteCodeMapper.selectById(inviteCode);
        if (code == null || Boolean.TRUE.equals(code.getIsUsed())) {
            throw new IllegalArgumentException("邀请码无效或已被使用");
        }
        if (userMapper.selectOne(new QueryWrapper<User>().eq("email", email)) != null) {
            throw new IllegalArgumentException("邮箱已注册");
        }
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setUsername(username);
        user.setInvitedByCode(inviteCode);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.insert(user);

        code.setIsUsed(true);
        code.setUsedBy(user.getId());
        code.setUsedAt(LocalDateTime.now());
        inviteCodeMapper.updateById(code);

        return user;
    }

    @Override
    public LoginResponse login(String email, String password) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("email", email));
        if (user == null || !passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Email or password error");
        }
        String token = JwtUtil.generateToken(user.getEmail());
        LoginResponse loginResponse = new LoginResponse();
        BeanUtils.copyProperties(user, loginResponse);
        loginResponse.setToken(token);
        return loginResponse;
    }

    @Override
    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;
        if (principal instanceof org.springframework.security.core.userdetails.User) {
            email = ((org.springframework.security.core.userdetails.User) principal).getUsername();
        } else if (principal instanceof CustomUserDetails) {
            email = ((CustomUserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            email = (String) principal;
        } else {
            throw new IllegalStateException("Unknown principal type: " + principal.getClass());
        }
        return userMapper.selectOne(new QueryWrapper<User>().eq("email", email));
    }

    @Override
    public void sendResetPasswordEmail(String email) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("email", email));
        if (user == null) {
            return;
        }

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpire(LocalDateTime.now().plusHours(1));
        userMapper.updateById(user);

        // Prepare email API request
        String apiUrl = "https://mail.cusob.com/api/batch_mail/api/send";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-Key", "1407534e6b439bf91bb146c56a66287bc9b29c8eb5c139356d0624082ba72651");
        headers.setContentType(MediaType.APPLICATION_JSON);

        String resetLink = "https://mail.cusob.com/reset-password?token=" + token;
        String body = String.format("{\"recipient\":\"%s\",\"addresser\":\"hello@cusob.com\",\"content\":\"Click to reset: %s\"}", email, resetLink);

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);
        System.out.println(response);
        // Optional: handle response status and errors
    }

    @Override
    public List<WebsiteDTO> getCurrentUserWebsites() {
        return null;
    }

    @Override
    public List<String> getCurrentUserDomains() {
        return List.of();
    }

    @Override
    public List<SubscriptionDTO> getCurrentUserSubscriptions() {
        return List.of();
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("reset_token", token));
        if (user == null || user.getResetTokenExpire().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Reset link is invalid or expired");
        }
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpire(null);
        userMapper.updateById(user);
    }
}