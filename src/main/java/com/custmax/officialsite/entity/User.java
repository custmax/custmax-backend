package com.custmax.officialsite.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("users")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String email;
    private String passwordHash;
    private String username;
    private String invitedByCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String resetToken;
    private LocalDateTime resetTokenExpire;
}