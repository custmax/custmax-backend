package com.custmax.officialsite.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: Vincent
 * @CreateTime: 2025-07-09
 * @Description: Response for whois domain availability query
 * @Version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WhoisResponse {
    private int code;
    private String email;
    private String phone;
    private String LLC;
    private String reg_url;
    private String whois_server;
    private List<String> domain_status;
    private List<String> dns;
    private String reg_date;
    private String updated_date;
    private String exp_date;
}