package com.example.blackbirdlofi.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
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

            // OAuth2 로그인 제공자 구분
            String provider = "";  // 로그인 제공자 구분 로직 추가
            if (oAuth2User.getAttributes().containsKey("sub")) { // 구글 로그인일 경우 "sub" 속성 존재
                provider = "google";
            } else if (oAuth2User.getAttributes().containsKey("id")) { // 스포티파이 로그인일 경우 "id" 속성 존재
                provider = "spotify";
            }

            model.addAttribute("provider", provider);  // 구글 or 스포티파이 제공자 정보
            model.addAttribute("userName", userName);  // 로그인한 사용자 이름
            model.addAttribute("userEmail", userEmail); // 로그인한 사용자 이메일


            // 세션 유지 확인 로그
            System.err.println("로그인된 사용자 이메일: " + userEmail);
            System.err.println("로그인된 사용자 이름: " + userName);
            System.err.println("OAuth2 로그인 제공자: " + provider);

        } else {
            model.addAttribute("userName", "Guest");
            System.err.println("로그인되지 않은 사용자: Guest");
        }

        return "usr/home/main"; // 메인 페이지 뷰
    }
}