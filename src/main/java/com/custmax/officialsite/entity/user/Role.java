// src/main/java/com/custmax/officialsite/entity/Role.java
package com.custmax.officialsite.entity.user;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("roles")
public class Role {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    @TableField("display_name")
    private String displayName;

    private String description;

    @TableField("is_system")
    private Boolean isSystem;

    private String status;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;

    @TableField("created_by")
    private Long createdBy;
}