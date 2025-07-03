package com.custmax.officialsite.dto.user;

import lombok.Data;

@Data
public class LoginResponse {
    private Long id;
    private String email;
    private String username;
    private String status;
    private String token;
}

