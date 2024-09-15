package com.example.blackbirdlofi.security;

import com.example.blackbirdlofi.jwt.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    public OAuth2AuthenticationSuccessHandler(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication authentication) throws IOException, ServletException {
        System.err.println("======================== OAuth2 로그인 성공 ========================");

        // OAuth2 인증 성공 시 사용자 정보 추출
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");  // OAuth2 로그인한 사용자의 이메일 가져오기
        System.err.println("로그인한 사용자 이메일: " + email);

        // JWT 생성
        String token = jwtTokenProvider.createToken(email, List.of("ROLE_USER"));  // 기본적으로 ROLE_USER 권한을 부여
        System.err.println("생성된 JWT: " + token);

        // JWT를 클라이언트에 전달 (헤더에 추가)
        res.addHeader("Authorization", "Bearer " + token);
        System.err.println("JWT를 응답 헤더에 추가함: Authorization: Bearer " + token);

        // 로그인 성공 후 리다이렉트 설정
        System.err.println("메인 페이지로 리다이렉트: /usr/home/main");
        getRedirectStrategy().sendRedirect(req, res, "/usr/home/main");
    }
}
