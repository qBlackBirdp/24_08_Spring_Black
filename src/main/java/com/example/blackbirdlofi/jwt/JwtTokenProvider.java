package com.example.blackbirdlofi.jwt;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    // 토큰 유효 시간 설정 (예: 10시간)
    private final long tokenValidTime = 1000L * 60 * 60 * 10;

    // secretKey를 Base64로 인코딩하여 초기화
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        System.err.println("JWT Secret Key Initialized: " + secretKey);
    }

    // JWT 토큰 생성 - email과 권한 정보를 함께 토큰에 포함
    public String createToken(String email, List<String> roles) {
        System.err.println("JWT 토큰 생성 요청: email=" + email + ", roles=" + roles);

        Claims claims = Jwts.claims().setSubject(email);  // 이메일을 subject로 설정
        claims.put("roles", roles);  // 사용자 권한 정보 추가

        Date now = new Date();
        Date expiration = new Date(now.getTime() + tokenValidTime);  // 토큰 유효 시간 설정

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)  // 발행 시간 설정
                .setExpiration(expiration)  // 만료 시간 설정
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 암호화 알고리즘 및 secretKey 사용
                .compact();

        System.err.println("JWT 토큰 생성 완료: " + token);

        return token;
    }

    // JWT 토큰에서 이메일 정보 추출
    public String getUserEmailFromToken(String token) {
        System.err.println("JWT 토큰에서 이메일 추출 요청: " + token);

        String email = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();  // 이메일(subject) 반환

        System.err.println("추출된 이메일: " + email);
        return email;
    }

    // JWT 토큰에서 사용자 권한 정보 추출
    public List<String> getUserRolesFromToken(String token) {
        System.err.println("JWT 토큰에서 권한 정보 추출 요청: " + token);

        List<String> roles = (List<String>) Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("roles");

        System.err.println("추출된 권한 정보: " + roles);
        return roles;
    }

    // JWT 토큰의 유효성 검증
    public boolean validateToken(String token) {
        System.err.println("JWT 토큰 유효성 검증 요청: " + token);

        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            System.err.println("토큰 유효성 검증 성공");
            return true;
        } catch (ExpiredJwtException e) {
            System.err.println("토큰이 만료되었습니다: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.err.println("지원되지 않는 토큰입니다: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.err.println("손상된 토큰입니다: " + e.getMessage());
        } catch (SignatureException e) {
            System.err.println("유효하지 않은 서명입니다: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("잘못된 토큰입니다: " + e.getMessage());
        }
        return false;
    }

    // JWT 토큰에서 만료 시간 확인 (필요 시 추가 기능)
    public boolean isTokenExpired(String token) {
        System.err.println("JWT 토큰 만료 시간 확인 요청: " + token);

        Date expiration = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        boolean isExpired = expiration.before(new Date());
        System.err.println("토큰 만료 여부: " + isExpired);
        return isExpired;
    }
}
