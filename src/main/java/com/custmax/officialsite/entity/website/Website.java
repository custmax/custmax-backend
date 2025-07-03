package com.custmax.officialsite.entity.website;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("websites")
public class Website {
    @TableId
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("domain_id")
    private Long domainId;

    private String name;

    @TableField("permalink_structure")
    private String permalinkStructure;

    private String content;

    private String settings;

    @TableField("is_multisite")
    private Boolean isMultisite;

    private String status;

    @TableField("created_at")
    private Timestamp createdAt;

    @TableField("updated_at")
    private Timestamp updatedAt;

    @TableField("moderation_status")
    private String moderationStatus;

    @TableField("moderation_reason")
    private String moderationReason;

    @TableField("moderation_date")
    private Timestamp moderationDate;

    @TableField("moderated_by")
    private Long moderatedBy;
}