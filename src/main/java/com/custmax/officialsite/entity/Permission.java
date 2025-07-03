package com.custmax.officialsite.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("permissions")
public class Permission {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    @TableField("display_name")
    private String displayName;

    private String description;

    private String resource;

    private String action;

    @TableField("is_system")
    private Boolean isSystem;

    private String status;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}