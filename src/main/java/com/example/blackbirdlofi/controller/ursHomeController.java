package com.example.blackbirdlofi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ursHomeController {

    @Autowired
    private OAuth2AuthorizedClientManager authorizedClientManager;


    @RequestMapping("/usr/home/main")
    public String showMain(@AuthenticationPrincipal OAuth2User principal, Model model, OAuth2AuthenticationToken authentication) {
        System.err.println("===================메인페이지 접근=====================");

        String provider = authentication.getAuthorizedClientRegistrationId(); // 로그인 제공자 감지
        model.addAttribute("provider", provider);  // JSP 페이지에서 제공자 정보 활용 가능

        if ("spotify".equals(provider)) {
            // Spotify 로그인일 때만 액세스 토큰 요청
            OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(
                    OAuth2AuthorizeRequest.withClientRegistrationId("spotify")
                            .principal(authentication)
                            .build());

            if (authorizedClient != null && authorizedClient.getAccessToken() != null) {
                String accessToken = authorizedClient.getAccessToken().getTokenValue();
                model.addAttribute("accessToken", accessToken);
                System.err.println("Spotify Access Token: " + accessToken);
            } else {
                System.err.println("Authorized client or access token is null.");
            }
        } else {
            // Google 로그인일 경우에는 Spotify API 호출하지 않도록 설정
            System.err.println("Google 로그인 감지 - Spotify API 호출 차단.");
            model.addAttribute("accessToken", null);  // 액세스 토큰 비활성화
        }

        return "usr/home/main"; // 메인 페이지 뷰
    }
}