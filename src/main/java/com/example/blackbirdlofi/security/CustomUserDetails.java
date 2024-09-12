package com.example.blackbirdlofi.security;

import com.example.blackbirdlofi.JPAentity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final Member member;

    public CustomUserDetails(Member member) {
        this.member = member;
    }

    // 권한 정보 (예: ROLE_USER) 등을 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한 설정 로직, 필요한 경우 구현
        return null;
    }

    @Override
    public String getPassword() {
        return member.getLoginPw();  // 비밀번호 반환
    }

    @Override
    public String getUsername() {
        return member.getUName();  // 사용자의 이름을 반환
    }

    public String getEmail() {
        return member.getEmail();  // 이메일 반환
    }

    // 계정이 만료되지 않았는지 반환
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정이 잠기지 않았는지 반환
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 자격 증명이 만료되지 않았는지 반환
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정이 활성화되었는지 반환
    @Override
    public boolean isEnabled() {
        return true;
    }
}
