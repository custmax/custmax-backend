package com.custmax.officialsite.service;

import com.custmax.officialsite.dto.ClaimResult;
import com.custmax.officialsite.entity.InviteCode;

public interface InviteCodeService {
    ClaimResult claim(String email);
    boolean send(String code, String email);
    boolean validate(String code);
    boolean use(String code, Long userId);
    InviteCode getByCode(String code);
}