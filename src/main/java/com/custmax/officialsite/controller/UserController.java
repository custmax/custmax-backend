package com.custmax.officialsite.controller;

import com.custmax.officialsite.dto.subscription.SubscriptionDTO;
import com.custmax.officialsite.dto.user.LoginRequest;
import com.custmax.officialsite.dto.user.LoginResponse;
import com.custmax.officialsite.entity.user.CustomUserDetails;
import com.custmax.officialsite.entity.user.User;
import com.custmax.officialsite.service.UserService;
import com.custmax.officialsite.service.WebSiteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "Manage user accounts and profiles")
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private WebSiteService webSiteService;

    /** Registers a new user with the provided email, password, username, and invite code.
     * @param email User's email address
     * @param password User's password
     * @param username User's username
     * @param inviteCode Optional invite code for registration
     * @return The registered User object
     */
    @Operation(summary = "Register a new user")
    @PostMapping("/register")
    public User register(@RequestParam String email, @RequestParam String password,
                         @RequestParam String username, @RequestParam String inviteCode) {
        return userService.register(email, password, username, inviteCode);
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
     * Retrieves the current user's subscription details.
     * @return
     */
    @Operation(summary = "Get current user details with authentication")
    @GetMapping("/me/subscription")
    public List<SubscriptionDTO> getUserSubscriptions() {
        return userService.getCurrentUserSubscriptions();
    }

    /**
     * Retrieves the current user's domains.
     * @return
     */
    @Operation(summary = "Get current user domains")
    @GetMapping("/me/domains")
    public List<String> getUserDomains() {
        return userService.getCurrentUserDomains();
    }

    /**
     * Lists all websites associated with the current user.
     * @param user The authenticated user
     * @return A list of websites for the user
     */
    @Operation(summary = "List websites for the current user")
    @GetMapping("/users/me/websites")
    public ResponseEntity<?> listUserWebsites(@AuthenticationPrincipal CustomUserDetails user) {
        List<?> websites = webSiteService.listUserWebsites(user.getUserId());
        return ResponseEntity.ok(websites);
    }

}