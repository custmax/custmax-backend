package com.custmax.officialsite.entity.user;

import com.baomidou.mybatisplus.annotation.*;
import com.custmax.officialsite.entity.subscription.Subscription;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("users")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String email;
    @TableField("password_hash")
    private String passwordHash;
    private String username;
    @TableField("invited_by_code")
    private String invitedByCode;
    @TableField("created_at")
    private LocalDateTime createdAt;
    @TableField("updated_at")
    private LocalDateTime updatedAt;
    @TableField("reset_token")
    private String resetToken;
    @TableField("reset_token_expire")
    private LocalDateTime resetTokenExpire;

    /**
     * User status: active, suspended, banned
     */
    public enum Status {
        active, suspended, banned
    }

    private String status;
    @TableField("status_reason")
    private String statusReason;
    @TableField("status_changed_at")
    private LocalDateTime statusChangedAt;
    @TableField("status_changed_by")
    private Long statusChangedBy;
    @TableField("last_login")
    private LocalDateTime lastLogin;
    @TableField("failed_login_attempts")
    private Integer failedLoginAttempts;
    @TableField("lockout_until")
    private LocalDateTime lockoutUntil;

    @TableField(exist = false)
    private Subscription subscription;

    @TableField(exist = false)
    private List<Role> roles;

    @TableField(exist = false)
    private List<Permission> permissions;
}