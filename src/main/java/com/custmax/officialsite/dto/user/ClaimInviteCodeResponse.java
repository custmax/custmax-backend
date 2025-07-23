package com.custmax.officialsite.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClaimInviteCodeResponse {
    private String code;
    private boolean existed;
}