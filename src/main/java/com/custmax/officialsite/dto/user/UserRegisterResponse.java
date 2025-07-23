package com.custmax.officialsite.dto.user;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: Vincent
 * @CreateTime: 2025-07-10
 * @Description:
 * @Version: 1.0
 */
@Data
public class UserRegisterResponse {
    private Long id;
    private String username;
    private String nickname;
    private String invitedByCode;
    private LocalDateTime createdAt;
}
