package com.custmax.officialsite.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("invite_codes")
public class InviteCode {
    @TableId
    private String code;
    private String email;
    private Boolean isUsed;
    private Long usedBy;
    private LocalDateTime sentAt;
    private Boolean sendSuccess;
    private LocalDateTime usedAt;
    private LocalDateTime createdAt;
}