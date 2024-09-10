package com.example.blackbirdlofi.controller.firebase;

import com.example.blackbirdlofi.service.firebase.FirebaseUserService;
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
            // Firebase에서 사용자가 있는지 확인하고, 로컬 DB에도 없는 경우 추가
            UserRecord userRecord = firebaseUserService.getUserByEmail(email);

            if (userRecord == null) {
                // 소셜 로그인 시에는 비밀번호를 넘기지 않음
                firebaseUserService.createUserInFirebase(email, null);  // 비밀번호 없이 Firebase 및 로컬 DB에 사용자 추가
                return "새 사용자가 Firebase와 로컬 DB에 추가되었습니다.";
            }

            return "User found: " + userRecord.getEmail();
        } catch (FirebaseAuthException e) {
            return "Error: " + e.getMessage();
        }
    }
}