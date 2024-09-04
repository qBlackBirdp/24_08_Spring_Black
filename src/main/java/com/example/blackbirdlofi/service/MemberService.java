package com.example.blackbirdlofi.service;

import com.example.blackbirdlofi.JPAentity.Member;
import com.example.blackbirdlofi.repository.MemberRepository;
import com.example.blackbirdlofi.vo.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class MemberService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String googleId = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        Optional<Member> memberOptional = memberRepository.findByGoogleLoginId(googleId);

        Member member;
        if (memberOptional.isPresent()) {
            member = memberOptional.get();
            member.setEmail(email);
            member.setUName(name);
        } else {
            member = new Member();
            member.setGoogleLoginId(googleId);
            member.setEmail(email);
            member.setUName(name);
        }
        memberRepository.save(member);

        return new DefaultOAuth2User(
                Collections.singletonList(() -> "ROLE_USER"),
                oAuth2User.getAttributes(),
                "name"
        );
    }

    // 이메일 중복 체크
    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElse(null);
    }

    // 회원가입 처리
    public ResultData doJoin(String loginId, String loginPw, String uName, String nickname, String email) {
        Member newMember = new Member();
        newMember.setLoginId(loginId);
        newMember.setLoginPw(loginPw);
        newMember.setUName(uName);
        newMember.setNickname(nickname);
        newMember.setEmail(email);

        memberRepository.save(newMember);
        return ResultData.from("S-1", "회원가입이 완료되었습니다.", "생성된 회원 id", newMember.getId());
    }

    public Member getMemberById(int id) {
        return memberRepository.findById(id).orElse(null);
    }
//    public Member getMemberById(int data1) {
//    }
}
