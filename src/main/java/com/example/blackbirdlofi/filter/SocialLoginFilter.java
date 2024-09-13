package com.example.blackbirdlofi.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SocialLoginFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // 소셜 로그인 세션이 유지 중일 때 로그인 페이지 접근을 막음
        if (auth != null && auth instanceof OAuth2AuthenticationToken) {
            // 사용자가 소셜 로그인 상태에서 로그인 페이지에 접근하려고 하면 메인 페이지로 리디렉션
            if (request.getRequestURI().contains("/usr/member/login")) {
                response.sendRedirect("/usr/home/main");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
