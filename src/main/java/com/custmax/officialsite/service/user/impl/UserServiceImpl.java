package com.custmax.officialsite.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.custmax.officialsite.dto.user.LoginResponse;
import com.custmax.officialsite.dto.user.UserRegisterRequest;
import com.custmax.officialsite.dto.user.UserRegisterResponse;
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
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private InviteCodeMapper inviteCodeMapper;

    @Resource
    private EmailSenderStrategyFactory emailSenderContext;

    @Value("${website.full-url}")
    private String websiteFullUrl;


    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserRegisterResponse register(UserRegisterRequest request) {
        InviteCode code = inviteCodeMapper.selectById(request.getInviteCode());
        if (code == null || Boolean.TRUE.equals(code.getIsUsed())) {
            throw new IllegalArgumentException("invite code is invalid or already used");
        }
        if (userMapper.selectOne(new QueryWrapper<User>().eq("username", request.getEmail())) != null) {
            throw new IllegalArgumentException("email already exists");
        }
        User user = new User();
        user.setUsername(request.getEmail());
        user.setNickname(request.getNickname());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setInvitedByCode(request.getEmail());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.insert(user);

        code.setIsUsed(true);
        code.setUsedBy(user.getId());
        code.setUsedAt(LocalDateTime.now());
        inviteCodeMapper.updateById(code);

        UserRegisterResponse response = new UserRegisterResponse();
        response.setId(user.getId());
        response.setNickname(request.getNickname());
        response.setUsername(request.getEmail());
        response.setCreatedAt(LocalDateTime.now());
        response.setInvitedByCode(request.getInviteCode());
        response.setCreatedAt(LocalDateTime.now());
        return response;
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


        emailSenderContext.sendResetPasswordEmail("smtp", email, resetUrl);

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