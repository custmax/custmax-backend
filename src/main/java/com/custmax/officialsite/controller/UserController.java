package com.custmax.officialsite.controller;

import com.custmax.officialsite.dto.domain.DomainDetailsResponse;
import com.custmax.officialsite.dto.user.LoginRequest;
import com.custmax.officialsite.dto.user.LoginResponse;
import com.custmax.officialsite.dto.user.UserRegisterRequest;
import com.custmax.officialsite.dto.user.UserRegisterResponse;
import com.custmax.officialsite.dto.website.GetUserWebsiteDetailsResponse;
import com.custmax.officialsite.entity.user.CustomUserDetails;
import com.custmax.officialsite.entity.user.User;
import com.custmax.officialsite.entity.website.Website;
import com.custmax.officialsite.service.domain.DomainService;
import com.custmax.officialsite.service.user.UserService;
import com.custmax.officialsite.service.website.WebSiteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "Manage user accounts and profiles")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private DomainService domainService;
    @Autowired
    private WebSiteService webSiteService;

    /** Registers a new user with the provided email, password, username, and invite code.
     * @param request The UserRegisterRequest containing user details
     * @return The registered User object
     */
    @Operation(summary = "Register a new user")
    @PostMapping("/register")
    public UserRegisterResponse register(@RequestBody UserRegisterRequest request) {
        return userService.register(request);
    }

    /**
     * Logs in a user with the provided email and password.
     * @param loginRequest
     * @return
     */
    @Operation(summary = "User login")
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest.getEmail(), loginRequest.getPassword());
    }

    /**
     * Sends a password reset email to the user with the specified email address.
     * @param email
     */
    @Operation(summary = "Forgot password")
    @PostMapping("/forgot-password")
    public void forgotPassword(@RequestParam String email) {
        userService.sendResetPasswordEmail(email);
    }

    /**
     * Resets the user's password using the provided token and new password.
     * @param token The reset token sent to the user's email
     * @param newPassword The new password to set
     */
    @Operation(summary = "Reset password using token")
    @PostMapping("/reset-password")
    public void resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        userService.resetPassword(token, newPassword);
    }

    /**
     * Retrieves the details of the currently authenticated user.
     * @return
     */
    @Operation(summary = "Get current user details")
    @GetMapping("/me")
    public User me() {
        return userService.getCurrentUser();
    }

    /**
     * Retrieves the current user's domains.
     * @return
     */
    @Operation(summary = "Get current user domains")
    @GetMapping("/me/domains")
    public ResponseEntity<List<DomainDetailsResponse>> getUserDomains(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(domainService.getCurrentUserDomains(user));
    }

    @Operation(summary = "Get current user websites")
    @GetMapping("/me/websites")
    public ResponseEntity<List<GetUserWebsiteDetailsResponse>> getUserWebsites(@AuthenticationPrincipal CustomUserDetails user) {
        List<GetUserWebsiteDetailsResponse> websites = webSiteService.getUserWebsites(user.getUserId());
        return ResponseEntity.ok(websites);
    }

}