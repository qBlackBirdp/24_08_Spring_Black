package com.example.blackbirdlofi.security;

import com.example.blackbirdlofi.JPAentity.Member;
import com.example.blackbirdlofi.jwt.JwtTokenProvider;
import com.example.blackbirdlofi.repository.MemberRepository;
import com.example.blackbirdlofi.service.firebase.FirebaseUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final FirebaseUserService firebaseUserService;  // FirebaseUserService 추가

    public CustomOAuth2UserService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider, FirebaseUserService firebaseUserService) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.firebaseUserService = firebaseUserService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 기본적으로 OAuth2User 정보를 가져옴
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String googleLoginId = oAuth2User.getAttribute("sub"); // Google ID

        System.out.println("Google OAuth2로 가져온 사용자 이메일: " + email);
        System.out.println("Google OAuth2로 가져온 사용자 ID: " + googleLoginId);

        // Firebase JWT 검증
        String firebaseJwt = userRequest.getAccessToken().getTokenValue();
        boolean isFirebaseTokenValid = firebaseUserService.verifyToken(firebaseJwt);

        if (!isFirebaseTokenValid) {
            throw new OAuth2AuthenticationException("Invalid Firebase Token");
        }

        // 로컬 DB에서 사용자 확인
        Optional<Member> localUser = memberRepository.findByGoogleLoginId(googleLoginId);
        if (localUser.isPresent()) {
            // JWT 발급
            String token = jwtTokenProvider.createToken(email, List.of("ROLE_USER"));
            System.out.println("기존 사용자에 대한 JWT 토큰 발급 완료: " + token);

            // 이미 존재하는 사용자 정보 반환
            return new CustomOAuth2User(localUser.get(), oAuth2User.getAttributes());
        } else {
            System.out.println("로컬 DB에 사용자가 존재하지 않음, 새로운 사용자 등록 시작");

            // 새로운 사용자 생성
            String displayName = oAuth2User.getAttribute("name") != null ? oAuth2User.getAttribute("name") : "사용자";
            String nickname = email.split("@")[0];
            createUserInLocalDB(email, displayName, nickname, googleLoginId);

            // JWT 발급
            String token = jwtTokenProvider.createToken(email, List.of("ROLE_USER"));
            System.out.println("새로운 사용자에 대한 JWT 토큰 발급 완료: " + token);

            // 새로 생성한 사용자 정보 반환
            Member newUser = memberRepository.findByGoogleLoginId(googleLoginId).orElseThrow();
            return new CustomOAuth2User(newUser, oAuth2User.getAttributes());
        }
    }

    // 로컬 DB에 사용자 생성
    private void createUserInLocalDB(String email, String uName, String nickname, String googleLoginId) {
        // 기존 사용자 중복 확인
        if (memberRepository.findByGoogleLoginId(googleLoginId).isPresent()) {
            System.out.println("이미 존재하는 사용자: " + googleLoginId);
            return; // 중복된 사용자면 추가로 생성하지 않고 그냥 리턴
        }

        System.out.println("로컬 DB에 사용자 생성 요청: email=" + email + ", 닉네임=" + nickname);
        Member newUser = new Member();
        newUser.setEmail(email);
        newUser.setUName(uName != null ? uName : "사용자");
        newUser.setNickname(nickname);
        newUser.setGoogleLoginId(googleLoginId);

        String loginId = "user-" + UUID.randomUUID().toString().substring(0, 8);
        newUser.setLoginId(loginId);

        memberRepository.save(newUser);
        System.out.println("로컬 DB에 사용자 저장 완료: " + email);
    }
}
