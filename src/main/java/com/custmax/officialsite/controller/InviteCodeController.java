package com.custmax.officialsite.controller;

import com.custmax.officialsite.entity.invite.InviteCode;
import com.custmax.officialsite.service.InviteCodeService;
import org.springframework.web.bind.annotation.*;
import com.custmax.officialsite.dto.user.ClaimInviteCodeResponse;
import javax.annotation.Resource;

@RestController
@RequestMapping("/api/invite-codes")
public class InviteCodeController {
    @Resource
    private InviteCodeService inviteCodeService;

    @PostMapping("/claim")
    public ClaimInviteCodeResponse claim(@RequestParam String email) {
        return inviteCodeService.claim(email);
    }

    @PostMapping("/send")
    public boolean send(@RequestParam String code, @RequestParam String email) {
        return inviteCodeService.send(code, email);
    }

    @GetMapping("/validate")
    public boolean validate(@RequestParam String code) {
        return inviteCodeService.validate(code);
    }

    @PostMapping("/use")
    public boolean use(@RequestParam String code, @RequestParam Long userId) {
        return inviteCodeService.use(code, userId);
    }

    @GetMapping("/{code}")
    public InviteCode getByCode(@PathVariable String code) {
        return inviteCodeService.getByCode(code);
    }
}