package com.example.blackbirdlofi.blackbirdlofi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ursHomeController {
    @RequestMapping("/usr/home/main")
    public String showMain() {
        return "/usr/home/main";
    }

    @RequestMapping("/urs/home/home")
    public String showHome() {
        return "/urs/home/home";
    }

    String sd = "11";
}
