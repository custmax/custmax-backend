package com.custmax.officialsite.dto.domain;

import lombok.Data;

/**
 * @Author: Vincent
 * @CreateTime: 2025-07-09
 * @Description:
 * @Version: 1.0
 */
@Data
public class RegisterDomainResponse {
    String message;
    String domainName;
    String status;
}
