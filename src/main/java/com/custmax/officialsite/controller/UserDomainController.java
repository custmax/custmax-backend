package com.custmax.officialsite.controller;

import com.custmax.officialsite.dto.SubscriptionDTO;
import com.custmax.officialsite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class UserDomainController {
    @Resource
    private UserService userService;
    @GetMapping("/me/domains")
    public List<String> getUserDomains() {
        // 假设 UserService 有方法获取用户域名
        return userService.getCurrentUserDomains();
    }

    @GetMapping("/me/subscription")
    public List<SubscriptionDTO> getUserSubscriptions() {
        return userService.getCurrentUserSubscriptions();
    }
}
