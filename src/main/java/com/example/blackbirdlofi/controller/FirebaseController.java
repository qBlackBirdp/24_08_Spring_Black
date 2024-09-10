package com.example.blackbirdlofi.controller;

import com.example.blackbirdlofi.service.FirebaseUserService;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirebaseController {

    private final FirebaseUserService firebaseUserService;

    @Autowired
    public FirebaseController(FirebaseUserService firebaseUserService) {
        this.firebaseUserService = firebaseUserService;
    }

    @GetMapping("/firebaseUser")
    public String getFirebaseUserByEmail(@RequestParam String email) {
        try {
            // Firebase에서 사용자가 있는지 확인하고, 없으면 Firebase와 로컬 DB에 사용자 생성
            UserRecord userRecord = firebaseUserService.getUserByEmail(email);

            // 만약 로컬 DB에 없고 Firebase에서도 없으면 새 사용자를 추가
            if (userRecord == null) {
                String password = "randomGeneratedPassword";  // 비밀번호가 필요할 경우 생성 (소셜 로그인 시에는 필요 없을 수 있음)
                firebaseUserService.createUserInFirebase(email, password);
                return "새 사용자가 Firebase와 로컬 DB에 추가되었습니다.";
            }

            return "User found: " + userRecord.getEmail();
        } catch (FirebaseAuthException e) {
            return "Error: " + e.getMessage();
        }
    }
}