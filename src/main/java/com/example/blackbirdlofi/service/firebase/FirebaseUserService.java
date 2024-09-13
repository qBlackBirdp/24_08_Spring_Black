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
        System.err.println("이메일로 Firebase 사용자 조회: " + email);

        Optional<Member> localUser = memberRepository.findByEmail(email);
        if (localUser.isPresent()) {
            System.out.println("로컬 DB에서 사용자 발견: " + localUser.get().getEmail());
            return null;
        }

        UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(email);
        System.err.println("Firebase에서 사용자 발견: " + userRecord.getEmail());

        String displayName = userRecord.getDisplayName() != null ? userRecord.getDisplayName() : "사용자";
        String nickname = email.split("@")[0];
        createUserInLocalDB(userRecord.getEmail(), displayName, nickname, userRecord.getUid());
        return userRecord;
    }

    // Firebase에 새 사용자 추가
    public void createUserInFirebase(String email, String loginPw) throws FirebaseAuthException {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(loginPw != null ? loginPw : "defaultPassword");

        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
        System.out.println("Firebase에 새 사용자 생성 완료: " + userRecord.getUid());

        String nickname = email.split("@")[0];
        createUserInLocalDB(email, userRecord.getDisplayName(), nickname, userRecord.getUid());
    }

    // 구글 로그인 처리 및 JWT 발급
    public String googleLogin(String idToken) throws FirebaseAuthException {
        System.err.println("구글 로그인 처리 시작 - Firebase ID 토큰 검증: " + idToken);
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
        String uid = decodedToken.getUid();
        String email = decodedToken.getEmail();
        System.out.println("Firebase에서 검증된 사용자 UID: " + uid);
        System.out.println("Firebase에서 검증된 사용자 이메일: " + email);

        Optional<Member> localUser = memberRepository.findByGoogleLoginId(uid);
        if (localUser.isPresent()) {
            System.err.println("로컬 DB에 사용자가 존재합니다. UID: " + uid);
            String token = jwtTokenProvider.createToken(email);
            System.err.println("기존 사용자에 대한 JWT 토큰 발급 완료: " + token);
            return token;  // JWT 발급
        } else {
            System.err.println("로컬 DB에 사용자가 존재하지 않아 새로 등록합니다.");
            createUserInFirebase(email, null);
            String token = jwtTokenProvider.createToken(email);
            System.err.println("새 사용자에 대한 JWT 토큰 발급 완료: " + token);
            return token;  // 회원가입 후 JWT 발급
        }
    }

    // firebase에 존재하지만 local DB에는 존재하지 않을 시 사용될 메서드
    private void createUserInLocalDB(String email, String uName, String nickname, String googleLoginId) {
        System.err.println("로컬 DB에 사용자 생성: email=" + email + ", 닉네임=" + nickname);
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