package com.example.blackbirdlofi.service.firebase;

import com.example.blackbirdlofi.JPAentity.Member;
import com.example.blackbirdlofi.jwt.JwtTokenProvider;
import com.example.blackbirdlofi.repository.MemberRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FirebaseUserService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // 이메일로 Firebase에서 사용자 정보 가져오기
    public UserRecord getUserByEmail(String email) throws FirebaseAuthException {
        System.err.println("이메일로 Firebase 사용자 조회 요청: " + email);

        // 로컬 DB에서 사용자 확인
        Optional<Member> localUser = memberRepository.findByEmail(email);
        if (localUser.isPresent()) {
            System.err.println("로컬 DB에서 사용자 발견: " + localUser.get().getEmail());
            return null;  // 이미 존재하는 사용자이므로 Firebase에서 추가 처리 불필요
        }

        // Firebase에서 사용자 정보 가져오기
        UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(email);
        System.err.println("Firebase에서 사용자 발견: " + userRecord.getEmail());

        // 로컬 DB에 사용자 등록
        String displayName = userRecord.getDisplayName() != null ? userRecord.getDisplayName() : "사용자";
        String nickname = email.split("@")[0];
        createUserInLocalDB(userRecord.getEmail(), displayName, nickname, userRecord.getUid());
        return userRecord;
    }

    // Firebase에 새 사용자 추가
    public void createUserInFirebase(String email, String loginPw) throws FirebaseAuthException {
        System.err.println("Firebase에 새 사용자 추가 요청: email=" + email);

        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(loginPw != null ? loginPw : "defaultPassword");

        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
        System.err.println("Firebase에 새 사용자 생성 완료: " + userRecord.getUid());

        String nickname = email.split("@")[0];
        createUserInLocalDB(email, userRecord.getDisplayName(), nickname, userRecord.getUid());
    }

    // Firebase ID 토큰을 사용해 Google 로그인 처리 및 JWT 발급
    public String googleLogin(String idToken) throws FirebaseAuthException {
        System.err.println("구글 로그인 처리 시작 - Firebase ID 토큰 검증 요청: " + idToken);

        // Firebase ID 토큰 검증
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
        String uid = decodedToken.getUid();
        String email = decodedToken.getEmail();
        System.err.println("Firebase에서 검증된 사용자 UID: " + uid);
        System.err.println("Firebase에서 검증된 사용자 이메일: " + email);

        // 로컬 DB에서 사용자 확인
        Optional<Member> localUser = memberRepository.findByGoogleLoginId(uid);
        if (localUser.isPresent()) {
            System.err.println("로컬 DB에 사용자가 존재합니다. UID: " + uid);
            // JWT 발급
            String token = jwtTokenProvider.createToken(email, List.of("ROLE_USER"));
            System.err.println("기존 사용자에 대한 JWT 토큰 발급 완료: " + token);
            return token;  // JWT 반환
        } else {
            System.err.println("로컬 DB에 사용자가 존재하지 않아 새로 등록합니다.");
            // 로컬 DB에 사용자 추가 후 JWT 발급
            createUserInFirebase(email, null);
            String token = jwtTokenProvider.createToken(email, List.of("ROLE_USER"));
            System.err.println("새 사용자에 대한 JWT 토큰 발급 완료: " + token);
            return token;  // JWT 반환
        }
    }

    // 로컬 DB에 사용자 생성
    private void createUserInLocalDB(String email, String uName, String nickname, String googleLoginId) {
        System.err.println("로컬 DB에 사용자 생성 요청: email=" + email + ", 닉네임=" + nickname);
        Member newUser = new Member();
        newUser.setEmail(email);
        newUser.setUName(uName != null ? uName : "사용자");
        newUser.setNickname(nickname);
        newUser.setGoogleLoginId(googleLoginId);

        String loginId = "user-" + UUID.randomUUID().toString().substring(0, 8);
        newUser.setLoginId(loginId);

        memberRepository.save(newUser);
        System.err.println("로컬 DB에 사용자 저장 완료: " + email);
    }
}
