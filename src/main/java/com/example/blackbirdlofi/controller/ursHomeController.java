package com.example.blackbirdlofi.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ursHomeController {


    @RequestMapping("/usr/home/main")
    public String showMain(@AuthenticationPrincipal OAuth2User oAuth2User, Model model ) {
        System.err.println("========================메인 페이지 접근============================");
        if (oAuth2User != null) {
            // 사용자의 이름과 이메일을 가져옴 (필요에 따라 수정)
            String userName = oAuth2User.getAttribute("name");
            String userEmail = oAuth2User.getAttribute("email");

            // 사용자 정보를 모델에 추가하여 JSP에서 출력 가능
            model.addAttribute("userName", userName);
            model.addAttribute("userEmail", userEmail);

            // 콘솔로그
            System.err.println("로그인된 사용자 이름: " + userName);
            System.err.println("로그인된 사용자 이메일: " + userEmail);
        } else {
            System.err.println("로그인된 사용자가 없습니다.");
        }

        return "usr/home/main";
    }
}