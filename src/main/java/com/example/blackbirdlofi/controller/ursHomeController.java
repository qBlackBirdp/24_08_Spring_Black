package com.example.blackbirdlofi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ursHomeController {
    @RequestMapping("/usr/home/main")
    public String showMain() {
        return "/usr/home/main";
    }

}