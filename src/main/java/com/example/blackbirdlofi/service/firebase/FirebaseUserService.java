package com.example.blackbirdlofi.service.firebase;

import com.example.blackbirdlofi.JPAentity.Member;
import com.example.blackbirdlofi.repository.MemberRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class FirebaseUserService {

    @Autowired
    private MemberRepository memberRepository;

    // 이메일로 Firebase에서 사용자 정보 가져오기
    public UserRecord getUserByEmail(String email) throws FirebaseAuthException {
        // 먼저 로컬 DB에서 사용자를 확인
        Optional<Member> localUser = memberRepository.findByEmail(email);
        if (localUser.isPresent()) {
            System.out.println("로컬 DB에서 사용자 발견: " + localUser.get().getEmail());
            return null;  // 로컬 DB에서 사용자를 찾으면 Firebase 조회를 건너뜀
        }

        // 로컬 DB에 없으면 Firebase에서 사용자 정보 조회
        UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(email);
        System.err.println("Firebase에서 사용자 발견: " + userRecord.getEmail());

        // Firebase에서 가져온 사용자 정보를 로컬 DB에 저장 (uName 추가)
        String displayName = userRecord.getDisplayName() != null ? userRecord.getDisplayName() : "사용자";  // displayName이 없을 경우 기본값 설정
        String nickname = email.split("@")[0];  // 이메일의 '@' 이전 부분을 닉네임으로 사용
        createUserInLocalDB(userRecord.getEmail(), displayName, nickname, userRecord.getUid());  // Google 로그인 ID를 UID로 저장
        return userRecord;
    }

    // Firebase에서 가져온 사용자 정보를 로컬 DB에 저장하는 메소드 (loginPw 제거)
    private void createUserInLocalDB(String email, String uName, String nickname, String googleLoginId) {
        Member newUser = new Member();
        newUser.setEmail(email);
        newUser.setUName(uName);  // Firebase에서 가져온 displayName을 uName으로 저장
        newUser.setNickname(nickname);  // 이메일 앞부분을 닉네임으로 저장
        newUser.setGoogleLoginId(googleLoginId);

        // 랜덤으로 생성된 loginId
        String loginId = "user-" + UUID.randomUUID().toString().substring(0, 8);  // UUID의 앞부분을 잘라서 사용
        newUser.setLoginId(loginId);

        // 비밀번호가 필요하지 않으므로 설정하지 않음

        memberRepository.save(newUser);  // 로컬 DB에 사용자 저장
        System.out.println("로컬 DB에 사용자 저장 완료: " + email);
    }

    // Firebase에 새 사용자 추가 (로컬 DB에도 추가)
    public void createUserInFirebase(String email, String loginPw) throws FirebaseAuthException {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(loginPw != null ? loginPw : "defaultPassword");  // Firebase에서는 비밀번호가 필요

        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
        System.out.println("Firebase에 새 사용자 생성 완료: " + userRecord.getUid());

        // 로컬 DB에도 사용자 정보 추가
        String nickname = email.split("@")[0];  // 이메일의 '@' 이전 부분을 닉네임으로 사용
        createUserInLocalDB(email, userRecord.getDisplayName(), nickname, userRecord.getUid());
    }
}