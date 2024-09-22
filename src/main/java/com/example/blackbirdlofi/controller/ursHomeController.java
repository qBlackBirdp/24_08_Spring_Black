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

        // 토큰을 관리하고 자동으로 갱신 처리
        OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(
                OAuth2AuthorizeRequest.withClientRegistrationId("spotify")
                        .principal(authentication)
                        .build());

        if (authorizedClient != null && authorizedClient.getAccessToken() != null) {
            String accessToken = authorizedClient.getAccessToken().getTokenValue();
            model.addAttribute("accessToken", accessToken);
        } else {
            System.err.println("Authorized client or access token is null.");
        }
        return "usr/home/main"; // 메인 페이지 뷰
    }
}