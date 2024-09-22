package com.example.blackbirdlofi.security;

import com.example.blackbirdlofi.JPAentity.Member;
import com.example.blackbirdlofi.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    public CustomOAuth2UserService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 기본적으로 OAuth2User 정보를 가져옴
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 어느 OAuth2 제공자인지 확인 (Google, Spotify 등)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String email;
        String externalLoginId;
        String profileImageUrl;

        // 구글인지 스포티파이인지 확인하여 처리
        if ("google".equals(registrationId)) {
            email = oAuth2User.getAttribute("email");
            externalLoginId = oAuth2User.getAttribute("sub"); // Google ID
            profileImageUrl = oAuth2User.getAttribute("picture"); // Google 프로필 이미지 URL
            System.out.println("Google OAuth2로 가져온 사용자 이메일: " + email);
            System.out.println("Google OAuth2로 가져온 사용자 ID: " + externalLoginId);

            // 세션에 프로필 이미지 URL 저장
            storeProfileImageInSession(profileImageUrl);

            return processGoogleUser(oAuth2User, email, externalLoginId);
        } else if ("spotify".equals(registrationId)) {
            email = oAuth2User.getAttribute("email");  // Spotify에서 제공되는 email
            externalLoginId = oAuth2User.getAttribute("id");  // Spotify 사용자 ID
            profileImageUrl = getSpotifyProfileImage(oAuth2User); // Spotify 프로필 이미지 URL
            System.out.println("Spotify OAuth2로 가져온 사용자 이메일: " + email);
            System.out.println("Spotify OAuth2로 가져온 사용자 ID: " + externalLoginId);

            // 세션에 프로필 이미지 URL 저장
            storeProfileImageInSession(profileImageUrl);

            return processSpotifyUser(oAuth2User, email, externalLoginId);
        } else {
            throw new OAuth2AuthenticationException("Unknown registrationId: " + registrationId);
        }
    }

    // Spotify 프로필 이미지 가져오기
    private String getSpotifyProfileImage(OAuth2User oAuth2User) {
        List<Map<String, Object>> images = oAuth2User.getAttribute("images");
        if (images != null && !images.isEmpty()) {
            return (String) images.get(0).get("url"); // 첫 번째 이미지 URL 가져오기
        }
        return null; // 이미지가 없으면 null 반환
    }

    // 세션에 프로필 이미지 URL 저장하는 메서드
    private void storeProfileImageInSession(String profileImageUrl) {
        // 현재 세션을 가져옴
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest().getSession();

        // 세션에 프로필 이미지 URL을 저장
        session.setAttribute("profileImageUrl", profileImageUrl);
    }

    // Google 사용자 처리
    private OAuth2User processGoogleUser(OAuth2User oAuth2User, String email, String googleLoginId) {
        Optional<Member> localUser = memberRepository.findByGoogleLoginId(googleLoginId);

        if (localUser.isPresent()) {
            return new CustomOAuth2User(localUser.get(), oAuth2User.getAttributes());
        } else {
            System.out.println("로컬 DB에 Google 사용자가 존재하지 않음, 새로운 사용자 등록 시작");

            String displayName = oAuth2User.getAttribute("name") != null ? oAuth2User.getAttribute("name") : "사용자";
            String nickname = email.split("@")[0];
            createGoogleUserInLocalDB(email, displayName, nickname, googleLoginId);

            Member newUser = memberRepository.findByGoogleLoginId(googleLoginId).orElseThrow();
            return new CustomOAuth2User(newUser, oAuth2User.getAttributes());
        }
    }

    // Spotify 사용자 처리
    private OAuth2User processSpotifyUser(OAuth2User oAuth2User, String email, String spotifyLoginId) {
        Optional<Member> localUser = memberRepository.findBySpotifyLoginId(spotifyLoginId);

        if (localUser.isPresent()) {
            return new CustomOAuth2User(localUser.get(), oAuth2User.getAttributes());
        } else {
            System.out.println("로컬 DB에 Spotify 사용자가 존재하지 않음, 새로운 사용자 등록 시작");

            String displayName = oAuth2User.getAttribute("display_name") != null ? oAuth2User.getAttribute("display_name") : "사용자";
            String nickname = email.split("@")[0];
            createSpotifyUserInLocalDB(email, displayName, nickname, spotifyLoginId);

            Member newUser = memberRepository.findBySpotifyLoginId(spotifyLoginId).orElseThrow();
            return new CustomOAuth2User(newUser, oAuth2User.getAttributes());
        }
    }

    // 로컬 DB에 Google 사용자 생성
    private void createGoogleUserInLocalDB(String email, String uName, String nickname, String googleLoginId) {
        if (memberRepository.findByGoogleLoginId(googleLoginId).isPresent()) {
            System.out.println("이미 존재하는 Google 사용자: " + googleLoginId);
            return;
        }

        Member newUser = new Member();
        newUser.setEmail(email);
        newUser.setUName(uName != null ? uName : "사용자");
        newUser.setNickname(nickname);
        newUser.setGoogleLoginId(googleLoginId);

        String loginId = "user-" + UUID.randomUUID().toString().substring(0, 8);
        newUser.setLoginId(loginId);

        memberRepository.save(newUser);
        System.out.println("로컬 DB에 Google 사용자 저장 완료: " + email);
    }

    // 로컬 DB에 Spotify 사용자 생성
    private void createSpotifyUserInLocalDB(String email, String uName, String nickname, String spotifyLoginId) {
        if (memberRepository.findBySpotifyLoginId(spotifyLoginId).isPresent()) {
            System.out.println("이미 존재하는 Spotify 사용자: " + spotifyLoginId);
            return;
        }

        Member newUser = new Member();
        newUser.setEmail(email);
        newUser.setUName(uName != null ? uName : "사용자");
        newUser.setNickname(nickname);
        newUser.setSpotifyLoginId(spotifyLoginId);

        String loginId = "user-" + UUID.randomUUID().toString().substring(0, 8);
        newUser.setLoginId(loginId);

        memberRepository.save(newUser);
        System.out.println("로컬 DB에 Spotify 사용자 저장 완료: " + email);
    }
}