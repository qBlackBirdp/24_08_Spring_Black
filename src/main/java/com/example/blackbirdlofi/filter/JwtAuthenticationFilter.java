package com.example.blackbirdlofi.filter;

import com.example.blackbirdlofi.jwt.JwtTokenProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 로그 찍기 - 요청 들어옴
        System.err.println("JwtAuthenticationFilter: 요청 들어옴 - " + request.getRequestURI());

        String token = resolveToken(request);
        System.err.println("JwtAuthenticationFilter: 추출된 토큰 - " + token);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            String userEmail = jwtTokenProvider.getUserEmailFromToken(token);
            System.err.println("JwtAuthenticationFilter: 토큰 유효함, 사용자 이메일 - " + userEmail);

            // JWT에서 추출한 사용자 이메일을 기반으로 인증 생성
            User authentication = new User(userEmail, "", new ArrayList<>());
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(authentication, null, authentication.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // SecurityContext에 인증 정보 설정
            SecurityContextHolder.getContext().setAuthentication(authToken);
            System.err.println("JwtAuthenticationFilter: 인증 정보 SecurityContext에 설정됨");
        } else {
            System.err.println("JwtAuthenticationFilter: 토큰이 없거나 유효하지 않음");
        }

        filterChain.doFilter(request, response);
        System.err.println("JwtAuthenticationFilter: 필터 체인 다음 필터로 넘어감");
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
