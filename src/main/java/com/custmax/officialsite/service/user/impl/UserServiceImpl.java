package com.custmax.officialsite.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.custmax.officialsite.dto.subscription.SubscriptionResponse;
import com.custmax.officialsite.dto.user.LoginResponse;
import com.custmax.officialsite.dto.website.WebsiteDTO;
import com.custmax.officialsite.entity.user.CustomUserDetails;
import com.custmax.officialsite.entity.user.User;
import com.custmax.officialsite.entity.invite.InviteCode;
import com.custmax.officialsite.mapper.UserMapper;
import com.custmax.officialsite.mapper.InviteCodeMapper;
import com.custmax.officialsite.service.email.impl.EmailSenderStrategyFactory;
import com.custmax.officialsite.service.user.UserService;
import com.custmax.officialsite.util.JwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
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

    @Value("${website.full-url}")
    private String websiteFullUrl;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User register(String email, String password, String username, String inviteCode) {
        InviteCode code = inviteCodeMapper.selectById(inviteCode);
        if (code == null || Boolean.TRUE.equals(code.getIsUsed())) {
            throw new IllegalArgumentException("invite code is invalid or already used");
        }
        if (userMapper.selectOne(new QueryWrapper<User>().eq("email", email)) != null) {
            throw new IllegalArgumentException("email already exists");
        }
        User user = new User();
        user.setUsername(email);
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
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", email));
        if (user == null || !passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Email or password error");
        }
        String token = JwtUtil.generateToken(user.getUsername());
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
        return userMapper.selectOne(new QueryWrapper<User>().eq("username", email));
    }


    @Override
    public void sendResetPasswordEmail(String email) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", email));
        if (user == null) {
            return;
        }

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpire(LocalDateTime.now().plusHours(1));
        userMapper.updateById(user);

        String resetUrl = websiteFullUrl + "/reset-password?token=" + token;

        EmailSenderStrategyFactory emailSenderContext = new EmailSenderStrategyFactory();
        emailSenderContext.sendResetPasswordEmail("smtp", email, resetUrl);


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
    public List<SubscriptionResponse> getCurrentUserSubscriptions() {
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