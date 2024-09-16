package com.example.blackbirdlofi.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ursHomeController {


    @RequestMapping("/usr/home/main")
    public String showMain(@AuthenticationPrincipal OAuth2User oAuth2User, Model model) {
        System.err.println("===================메인페이지 접근=====================");
        if (oAuth2User != null) {
            model.addAttribute("userName", oAuth2User.getAttribute("name"));  // 로그인한 사용자 정보 전달
            model.addAttribute("userEmail", oAuth2User.getAttribute("email")); // 사용자 이메일
            System.err.println("userEmail : " + oAuth2User.getAttribute("email"));
        } else {
            model.addAttribute("userName", "Guest");
        }

        return "usr/home/main";
    }
}