package com.custmax.officialsite.dto.user;

import lombok.Data;

/**
 * @Author: Vincent
 * @CreateTime: 2025-07-08
 * @Description: dto request for user registeration
 * @Version: 1.0
 */
@Data
public class UserRegisterRequest {
    String email;
    String password;
    String nickname;
    String inviteCode;
}
