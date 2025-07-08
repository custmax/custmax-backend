package com.custmax.officialsite.service.invite;

import com.custmax.officialsite.dto.user.ClaimInviteCodeResponse;
import com.custmax.officialsite.entity.invite.InviteCode;

public interface InviteCodeService {
    ClaimInviteCodeResponse claim(String email);
    boolean send(String code, String email);
    boolean validate(String code);
    boolean use(String code, Long userId);
    InviteCode getByCode(String code);
}