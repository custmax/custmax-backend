package com.custmax.officialsite.entity.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.sql.Timestamp;

@Data
@TableName("domains")
public class Domain {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("subscription_id")
    private Long subscriptionId;

    @TableField("domain_name")
    private String domainName;

    @TableField("is_custom")
    private Boolean isCustom;

    @TableField("status")
    private Status status;

    @TableField("ssl_enabled")
    private Boolean sslEnabled;

    @TableField("created_at")
    private Timestamp createdAt;

    @TableField("updated_at")
    private Timestamp updatedAt;

    @TableField("moderation_status")
    private ModerationStatus moderationStatus;

    @TableField("moderation_reason")
    private String moderationReason;

    @TableField("moderation_date")
    private Timestamp moderationDate;

    @TableField("moderated_by")
    private Long moderatedBy;

    @TableField("verification_token")
    private String verificationToken;

    @TableField("verified_at")
    private Timestamp verifiedAt;

    @TableField("dns_configured")
    private Boolean dnsConfigured;

    public enum Status {
        active, pending, expired
    }

    public enum ModerationStatus {
        approved, pending_review, rejected, banned
    }
}