package com.example.securitydemo.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityDemoController {

    @GetMapping("/")
    public String home() {
        return "Home - public access";
    }

    @GetMapping("/user")
    public String user() {
        return "User endpoint - access granted";
    }

    @GetMapping("/admin")
    public String admin() {
        return "Admin endpoint - access granted";
    }
}


