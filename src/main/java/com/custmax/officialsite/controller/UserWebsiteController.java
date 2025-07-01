package com.custmax.officialsite.controller;

import com.custmax.officialsite.dto.WebsiteDTO;
import com.custmax.officialsite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UserWebsiteController {
    @Autowired
    UserService userService;

    // List User Websites
    @GetMapping("/me/websites")
    public List<WebsiteDTO> getUserWebsites() {
        return userService.getCurrentUserWebsites();
    }
}
