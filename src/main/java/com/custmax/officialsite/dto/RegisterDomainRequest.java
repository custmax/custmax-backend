package com.custmax.officialsite.dto;

import lombok.Data;

@Data
public class RegisterDomainRequest {
    String oldUrl;
    String newUrl;
}
