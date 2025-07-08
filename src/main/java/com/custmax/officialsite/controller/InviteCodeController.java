package com.custmax.officialsite.controller;

import com.custmax.officialsite.entity.invite.InviteCode;
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


    /**
     * Sends the invite code to the specified email.
     * @param code the invite code to send
     * @param email the email address to send the code to
     * @return true if the code was sent successfully, false otherwise
     */
    @Operation(summary = "Send invite code to email")
    @PostMapping("/send")
    public boolean send(@RequestParam String code, @RequestParam String email) {
        return inviteCodeService.send(code, email);
    }

    /**
     * Validates the invite code.
     * @param code the invite code to validate
     * @return true if the code is valid, false otherwise
     */
    @Operation(summary = "Validate an invite code")
    @GetMapping("/validate")
    public boolean validate(@RequestParam String code) {
        return inviteCodeService.validate(code);
    }

    /**
     * Uses the invite code for a user.
     * @param code the invite code to use
     * @param userId the ID of the user using the code
     * @return true if the code was used successfully, false otherwise
     */
    @Operation(summary = "Use an invite code")
    @PostMapping("/use")
    public boolean use(@RequestParam String code, @RequestParam Long userId) {
        return inviteCodeService.use(code, userId);
    }

    /**
     * Retrieves an invite code by its code string.
     * @param code the invite code string
     * @return the InviteCode object if found, null otherwise
     */
    @Operation(summary = "Get invite code by code string")
    @GetMapping("/{code}")
    public InviteCode getByCode(@PathVariable String code) {
        return inviteCodeService.getByCode(code);
    }
}