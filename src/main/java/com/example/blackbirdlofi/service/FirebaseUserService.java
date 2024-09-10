package com.example.blackbirdlofi.service;

import com.example.blackbirdlofi.JPAentity.Member;
import com.example.blackbirdlofi.repository.MemberRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class FirebaseUserService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // 이메일로 Firebase에서 사용자 정보 가져오기 (없으면 새 사용자 생성)
    public UserRecord getUserByEmail(String email) throws FirebaseAuthException {
        // 로컬 DB에서 사용자 확인
        Optional<Member> localUser = memberRepository.findByEmail(email);
        if (localUser.isPresent()) {
            System.out.println("로컬 DB에서 사용자 발견: " + localUser.get().getEmail());
            return null;  // 로컬 DB에서 사용자를 찾으면 Firebase 조회를 건너뜀
        }

        // 로컬 DB에 사용자가 없을 때 Firebase에서 사용자 정보 조회
        try {
            UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(email);
            System.err.println("Firebase에서 사용자 발견: " + userRecord.getEmail());

            // Firebase에서 가져온 사용자 정보를 로컬 DB에 저장
            createUserInLocalDB(userRecord.getEmail(), null, userRecord.getUid());
            return userRecord;

        } catch (FirebaseAuthException e) {
            // Firebase에 사용자도 없으면 새 사용자를 생성
            System.err.println("Firebase에서 사용자 없음, 새로 생성 중...");
            return null;  // 사용자가 없으면 null 반환
        }
    }

    // Firebase에서 가져온 사용자 정보를 로컬 DB에 저장하는 메소드
    private void createUserInLocalDB(String email, String password, String googleLoginId) {
        Member newUser = new Member();
        newUser.setEmail(email);
        newUser.setGoogleLoginId(googleLoginId);

        // loginId 자동 생성 (user-랜덤UUID 형식)
        String loginId = "user-" + UUID.randomUUID().toString().substring(0, 8);
        newUser.setLoginId(loginId);

        // 비밀번호가 제공된 경우에만 암호화하여 저장
        if (password != null) {
            newUser.setLoginPw(passwordEncoder.encode(password));  // 비밀번호 암호화
        }

        memberRepository.save(newUser);  // 로컬 DB에 사용자 저장
        System.out.println("로컬 DB에 사용자 저장 완료: " + email);
    }

    // Firebase에 새 사용자 추가 (로컬 DB에도 추가 가능)
    public void createUserInFirebase(String email, String password) throws FirebaseAuthException {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password);  // 필요한 경우 비밀번호 설정
        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
        System.out.println("Successfully created new user in Firebase: " + userRecord.getUid());

        // 로컬 DB에도 사용자 정보 추가
        createUserInLocalDB(email, password, userRecord.getUid());
    }
}