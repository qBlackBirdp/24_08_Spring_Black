package com.example.blackbirdlofi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

        if (auth != null && auth instanceof OAuth2AuthenticationToken) {
            // 소셜 로그인 상태라면 로컬 로그인을 막기 위한 로직을 추가
            if (request.getRequestURI().contains("/usr/member/doLocalLogin")) {
                response.sendRedirect("/usr/home/main");
                return;
            }
        }

        // 로컬 로그인 상태에서 소셜 로그인을 막기 위한 로직
        if (auth != null && auth instanceof UsernamePasswordAuthenticationToken) {
            if (request.getRequestURI().contains("/oauth2/authorization/google")) {
                response.sendRedirect("/usr/home/main");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}

