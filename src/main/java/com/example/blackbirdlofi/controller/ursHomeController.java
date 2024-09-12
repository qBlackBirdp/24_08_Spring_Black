package com.example.blackbirdlofi.controller;

import com.example.blackbirdlofi.JPAentity.Member;
import com.example.blackbirdlofi.vo.Rq;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ursHomeController {

    @Autowired
    private Rq rq;

    @RequestMapping("/usr/home/main")
    public String showMain(HttpServletRequest request) {
        // SecurityContextHolder에 저장된 인증 정보 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            System.out.println("로그인된 사용자: " + authentication.getName());
        } else {
            System.out.println("로그인된 사용자 없음");
        }

        // 세션에서 로그인된 사용자 정보 확인
        Member loginedMember = (Member) request.getSession().getAttribute("loginedMember");
        if (loginedMember != null) {
            System.out.println("세션에 저장된 사용자: " + loginedMember.getEmail());
        } else {
            System.out.println("세션에 저장된 사용자 없음");
        }

        return "usr/home/main";
    }
}