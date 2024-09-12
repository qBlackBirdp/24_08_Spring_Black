package com.example.blackbirdlofi.filter;

import com.example.blackbirdlofi.vo.Rq;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CustomLoginFilter extends OncePerRequestFilter {

    @Autowired
    private Rq rq;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 요청한 경로를 확인
        String requestURI = request.getRequestURI();

        // 로그인 페이지나 회원가입 페이지, 메인 페이지에 대해서는 필터링을 제외함
        if (requestURI.equals("/usr/member/login") || requestURI.equals("/usr/member/join") || requestURI.equals("/usr/home/main") || requestURI.equals("/usr/member/doLocalLogin")) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            // Rq 클래스를 사용해 경고 메시지 출력
            rq.printHistoryBack("로그인 후 이용해주세요.");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
