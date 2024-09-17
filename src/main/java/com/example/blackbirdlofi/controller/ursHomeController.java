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
            String userName = oAuth2User.getAttribute("name");
            String userEmail = oAuth2User.getAttribute("email");

            model.addAttribute("userName", userName);  // 로그인한 사용자 이름
            model.addAttribute("userEmail", userEmail); // 로그인한 사용자 이메일

            // 세션 유지 확인 로그
            System.err.println("로그인된 사용자 이메일: " + userEmail);
            System.err.println("로그인된 사용자 이름: " + userName);
        } else {
            model.addAttribute("userName", "Guest");
            System.err.println("로그인되지 않은 사용자: Guest");
        }

        return "usr/home/main"; // 메인 페이지 뷰
    }
}