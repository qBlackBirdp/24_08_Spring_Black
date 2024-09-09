package com.example.blackbirdlofi.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.springframework.stereotype.Service;

@Service
public class FirebaseUserService {

    // 이메일로 Firebase에서 사용자 정보 가져오기
    public UserRecord getUserByEmail(String email) throws FirebaseAuthException {
        System.err.println("getUserByEmail" + FirebaseAuth.getInstance().getUserByEmail(email));
        return FirebaseAuth.getInstance().getUserByEmail(email);
    }

    // UID로 사용자 정보 가져오기
    public UserRecord getUserByUid(String uid) throws FirebaseAuthException {
        System.err.println("getUser" + FirebaseAuth.getInstance().getUser(uid));
        return FirebaseAuth.getInstance().getUser(uid);
    }

    // Firebase에 새 사용자 추가
    public void createUser(String email, String password) throws FirebaseAuthException {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password);
        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
        System.out.println("Successfully created new user: " + userRecord.getUid());
    }
}