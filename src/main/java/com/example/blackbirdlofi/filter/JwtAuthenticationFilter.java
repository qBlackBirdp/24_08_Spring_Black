package com.example.blackbirdlofi.filter;

import com.example.blackbirdlofi.jwt.JwtTokenProvider;
import com.example.blackbirdlofi.service.firebase.FirebaseUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// 로그인 시에만 JWT를 사용하는 필터로 변경
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final FirebaseUserService firebaseUserService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, FirebaseUserService firebaseUserService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.firebaseUserService = firebaseUserService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = getJwtFromRequest(request);

        if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
            String userEmail = jwtTokenProvider.getUserEmailFromJWT(jwt);

            // Firebase JWT 검증 로직 추가
            if (!firebaseUserService.verifyToken(jwt)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Firebase Token");
                return;
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                // 인증 정보 설정
                UserDetails userDetails = jwtTokenProvider.getUserDetailsFromToken(jwt);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        System.out.println("Authorization 헤더: " + bearerToken); // 추가된 로깅
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}