package com.custmax.officialsite.service.impl;

import ch.qos.logback.classic.Logger;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.custmax.officialsite.dto.user.ClaimInviteCodeResponse;
import com.custmax.officialsite.entity.invite.InviteCode;
import com.custmax.officialsite.mapper.InviteCodeMapper;
import com.custmax.officialsite.service.InviteCodeService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
public class InviteCodeServiceImpl implements InviteCodeService {

    @Autowired
    private static final org.slf4j.Logger logger = (Logger) LoggerFactory.getLogger(InviteCodeServiceImpl.class);
    @Resource
    private InviteCodeMapper inviteCodeMapper;

    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private String generateInviteCode() {
        int length = 6 + (int)(Math.random() * 3);
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int idx = (int)(Math.random() * CHAR_POOL.length());
            sb.append(CHAR_POOL.charAt(idx));
        }
        return sb.toString();
    }

    private String generateUniqueInviteCode() {
        String code;
        do {
            code = generateInviteCode();
        } while (inviteCodeMapper.selectOne(
                new LambdaQueryWrapper<InviteCode>().eq(InviteCode::getCode, code)
        ) != null);
        return code;
    }

    @Override
    public ClaimInviteCodeResponse claim(String email) {
        // 查询该邮箱是否已存在未使用的邀请码
        InviteCode exist = inviteCodeMapper.selectOne(
                new LambdaQueryWrapper<InviteCode>()
                        .eq(InviteCode::getEmail, email)
                        .eq(InviteCode::getIsUsed, false)
        );
        if (exist != null) {
            // 已有未使用的邀请码，不再发送邮件
            return new ClaimInviteCodeResponse(exist.getCode(), true);
        }
        String code = generateUniqueInviteCode();
        InviteCode inviteCode = new InviteCode();
        inviteCode.setCode(code);
        inviteCode.setEmail(email);
        inviteCode.setIsUsed(false);
        inviteCode.setCreatedAt(LocalDateTime.now());
        inviteCodeMapper.insert(inviteCode);

        // 发送邮件
        boolean sendSuccess = sendInviteConfirmation(email, code);
        if (sendSuccess == false) {
            logger.error("failed to send invite code to email: {}", email);
        }
        return new ClaimInviteCodeResponse(code, false);
    }

    @Override
    public boolean send(String code, String email) {
        InviteCode inviteCode = inviteCodeMapper.selectById(code);
        if (inviteCode == null) {
            return false;
        }
        // TODO:
        boolean sendSuccess = sendInviteConfirmation(email, code);
        inviteCode.setSentAt(LocalDateTime.now());
        inviteCode.setSendSuccess(sendSuccess);
        inviteCodeMapper.updateById(inviteCode);
        return sendSuccess;
    }

    /**
     * send invite code successfully claimed by user to email
     * @param email
     * @param code
     * @return
     */
    private boolean sendInviteConfirmation(String email, String code) {
        try {
            java.net.URL url = new java.net.URL("https://mail.cusob.com/api/batch_mail/api/send");
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("X-API-Key", "fae86316b2ab703b16466dc0bc8fbbbe6526c43d53e3e18af6f16a18dccab15b");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String json = String.format("{\"recipient\": \"%s\"}", email);
            try (java.io.OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            }
            logger.info(conn.getResponseMessage());
            int responseCode = conn.getResponseCode();
            return responseCode == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public boolean validate(String code) {
        InviteCode inviteCode = inviteCodeMapper.selectById(code);
        return inviteCode != null && Boolean.FALSE.equals(inviteCode.getIsUsed());
    }

    @Override
    public boolean use(String code, Long userId) {
        InviteCode inviteCode = inviteCodeMapper.selectById(code);
        if (inviteCode == null || Boolean.TRUE.equals(inviteCode.getIsUsed())) {
            return false;
        }
        inviteCode.setIsUsed(true);
        inviteCode.setUsedBy(userId);
        inviteCode.setUsedAt(LocalDateTime.now());
        inviteCodeMapper.updateById(inviteCode);
        return true;
    }

    @Override
    public InviteCode getByCode(String code) {
        return inviteCodeMapper.selectById(code);
    }
}