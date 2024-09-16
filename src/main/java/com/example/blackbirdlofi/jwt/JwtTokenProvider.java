package com.example.blackbirdlofi.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    private final long expirationTime = 1000L * 60L * 60L * 12L; // 12시간 만료 시간
    private final JwtParser jwtParser;

    // 생성자에서 JwtParser 초기화
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        this.secretKey = secretKey;
        this.jwtParser = Jwts.parser().setSigningKey(secretKey); // 파서 재사용
    }

    // JWT 토큰 생성
    public String createToken(String email, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(email); // 이메일을 subject로 설정
        claims.put("roles", roles); // 역할 추가

        Date now = new Date();
        Date validity = new Date(now.getTime() + expirationTime);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        System.out.println("생성된 JWT 토큰: " + token); // 토큰 출력

        return token;
    }

    // JWT에서 사용자 이메일 추출
    public String getUserEmailFromJWT(String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody(); // 파서 재사용
        return claims.getSubject(); // subject에서 이메일 추출
    }

    // JWT 유효성 검사
    public boolean validateToken(String token) {
        try {
            jwtParser.parseClaimsJws(token); // JWT 파싱 및 검증
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("토큰이 만료되었습니다. JWT: " + token);
        } catch (MalformedJwtException e) {
            System.out.println("잘못된 JWT 형식입니다. JWT: " + token);
        } catch (SignatureException e) {
            System.out.println("JWT 서명이 잘못되었습니다. JWT: " + token);
        } catch (UnsupportedJwtException e) {
            System.out.println("지원하지 않는 JWT입니다. JWT: " + token);
        } catch (IllegalArgumentException e) {
            System.out.println("JWT 토큰이 비어있습니다.");
        }
        return false; // 유효하지 않은 경우 false 반환
    }

    public UserDetails getUserDetailsFromToken(String jwt) {
        // JWT에서 사용자 이메일 추출
        String userEmail = getUserEmailFromJWT(jwt);

        // 이메일을 통해 UserDetails 가져오기 (DB 혹은 UserDetailsService와 연동)
        return new org.springframework.security.core.userdetails.User(
                userEmail,
                "", // JWT를 이용한 인증에서는 비밀번호는 불필요
                true, // 계정 활성화 상태 (필요에 따라 수정 가능)
                true, // 계정 만료 여부
                true, // 비밀번호 만료 여부
                true, // 계정 잠김 여부
                List.of(new SimpleGrantedAuthority("ROLE_USER")) // 역할 설정
        );
    }
}
