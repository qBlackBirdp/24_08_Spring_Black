package com.example.blackbirdlofi.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @PostConstruct
    public void init() {
        System.err.println("JWT Secret Key: " + secretKey); // 이 부분으로 확인
        System.err.println("JWT Secret Key: " + secretKey); // 이 부분으로 확인
        System.err.println("JWT Secret Key: " + secretKey); // 이 부분으로 확인
    }

    // 토큰 유효 시간 설정 (예: 10시간)
    private final long tokenValidTime = 1000L * 60 * 60 * 10;

    // JWT 토큰 생성
    public String createToken(String email) {
        System.err.println("JWT 토큰 생성 요청: email=" + email);
        Claims claims = Jwts.claims().setSubject(email);
        Date now = new Date();
        Date expiration = new Date(now.getTime() + tokenValidTime);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        System.err.println("JWT 토큰 생성 완료: " + token);

        return token;
    }

    // JWT 토큰에서 이메일 정보 추출
    public String getUserEmailFromToken(String token) {
        System.err.println("토큰에서 이메일 정보 추출: " + token);
        String email = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        System.err.println("추출된 이메일: " + email);
        return email;
    }

    // JWT 토큰의 유효성 검증
    public boolean validateToken(String token) {
        System.err.println("토큰 유효성 검증 요청: " + token);
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            System.err.println("토큰 유효성 검증 성공");
            return true;
        } catch (Exception e) {
            System.err.println("토큰 유효성 검증 실패: " + e.getMessage());
            return false;
        }
    }
}
