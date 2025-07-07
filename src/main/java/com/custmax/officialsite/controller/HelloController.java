package com.custmax.officialsite.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello/paided")
    @PreAuthorize("hasRole('Paid')")
    public String hello() {
        return "Hello, World!";
    }

    @GetMapping("/hello/premium")
    @PreAuthorize("hasRole('premium')")
    public String helloPremium() {
        return "Hello, Premium World!";
    }
}
