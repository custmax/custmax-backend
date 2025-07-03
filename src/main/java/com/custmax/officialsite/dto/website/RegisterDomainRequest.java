package com.custmax.officialsite.dto.website;

import lombok.Data;

@Data
public class RegisterDomainRequest {
    String oldUrl;
    String newUrl;
}

