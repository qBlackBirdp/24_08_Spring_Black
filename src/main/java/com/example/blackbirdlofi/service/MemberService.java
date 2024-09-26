package com.example.blackbirdlofi.service;

import com.example.blackbirdlofi.JPAentity.Member;
import com.example.blackbirdlofi.repository.MemberRepository;
import com.example.blackbirdlofi.vo.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@Service
public class MemberService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>  {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;  // BCryptPasswordEncoder 주입

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
    @Transactional
    public ResultData doJoin(String loginId, String loginPw, String uName, String nickname, String email) {
        // 비밀번호 암호화 처리
        String encodedPassword = passwordEncoder.encode(loginPw);

        Member newMember = new Member();
        newMember.setLoginId(loginId);
        newMember.setLoginPw(encodedPassword);  // 암호화된 비밀번호 저장
        newMember.setUName(uName);
        newMember.setNickname(nickname);
        newMember.setEmail(email);
        newMember.setRegDate(LocalDateTime.now());
        newMember.setUpdateDate(LocalDateTime.now());

        try {
            memberRepository.save(newMember);
        } catch (Exception e) {
            System.err.println("Error saving member: " + e.getMessage());
            e.printStackTrace();
            return ResultData.from("F-1", "회원가입 실패");
        }

        return ResultData.from("S-1", "회원가입이 완료되었습니다.", "생성된 회원 id", newMember.getId());
    }

    public Member getMemberById(int id) {
        return memberRepository.findById(id).orElse(null);
    }

    public ResultData<Member> doLogin(String email, String loginPw) {
        // 이메일로 사용자를 찾음
        Member member = getMemberByEmail(email);
        if (member == null) {
            return ResultData.from("F-1", "해당 이메일로 가입된 사용자가 없습니다.");
        }

        // 비밀번호가 일치하는지 확인
        if (!passwordEncoder.matches(loginPw, member.getLoginPw())) {
            return ResultData.from("F-2", "비밀번호가 일치하지 않습니다.");
        }

        // 로그인 성공 시
        return ResultData.from("S-1", "로그인 성공", "member", member);
    }

}
