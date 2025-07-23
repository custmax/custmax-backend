package com.custmax.officialsite.controller;

import com.custmax.officialsite.service.invite.InviteCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import com.custmax.officialsite.dto.user.ClaimInviteCodeResponse;
import javax.annotation.Resource;

@RestController
@RequestMapping("/api/invite-codes")
@Tag(name = "Invite Code Management", description = "Manage invite codes for user registration")
public class InviteCodeController {
    @Resource
    private InviteCodeService inviteCodeService;

    /**
     * Generates a new invite code.
     * @return the generated invite code
     */
    @Operation(summary = "Generate a new invite code")
    @PostMapping("/claim")
    public ClaimInviteCodeResponse claim(@RequestParam String email) {
        return inviteCodeService.claim(email);
    }

}