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
public class CustomLogoutFilter extends OncePerRequestFilter {

    @Autowired
    private Rq rq;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 요청한 경로를 확인
        String requestURI = request.getRequestURI();

        // 메인 페이지(/usr/home/main)는 필터링을 제외 (로그인/로그아웃 여부와 상관없이 접근 가능)
        if (requestURI.equals("/usr/home/main")) {
            filterChain.doFilter(request, response);
            return;
        }

        // SecurityContext에서 현재 인증된 사용자 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 로그인이 되어 있다면
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            // Rq 객체를 생성하고 메시지 출력
            rq.printHistoryBack("로그아웃 후 이용해주세요.");
            return;  // 요청 중단
        }

        // 로그아웃 상태라면 요청을 계속 진행
        filterChain.doFilter(request, response);
    }
}
