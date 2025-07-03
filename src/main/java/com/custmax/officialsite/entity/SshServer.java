package com.custmax.officialsite.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SshServer {
    private Long id;
    private String host;
    private Integer port;
    private String username;
    private String password;
    private Integer status;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}