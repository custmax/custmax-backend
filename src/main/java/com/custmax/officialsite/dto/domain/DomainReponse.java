package com.custmax.officialsite.dto.domain;

import com.custmax.officialsite.entity.domain.Domain;
import lombok.Data;

@Data
public class DomainReponse {
    Long id;
    String domainName;
    Domain.Status domainStatus;
    Boolean sslEnabled;
}
