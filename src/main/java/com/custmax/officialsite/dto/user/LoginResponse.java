package com.custmax.officialsite.dto.user;

import com.custmax.officialsite.entity.user.User;
import lombok.Data;

@Data
public class LoginResponse {
    private Long id;
    private String username;
    private User.Status status;
    private String token;
}

