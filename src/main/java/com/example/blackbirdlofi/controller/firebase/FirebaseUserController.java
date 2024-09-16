package com.example.blackbirdlofi.controller.firebase;

import com.example.blackbirdlofi.service.firebase.FirebaseUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirebaseUserController {

    private final FirebaseUserService firebaseUserService;

    public FirebaseUserController(FirebaseUserService firebaseUserService) {
        this.firebaseUserService = firebaseUserService;
    }

    @GetMapping("/firebaseUser")
    public ResponseEntity<?> getFirebaseUser(@RequestParam String email) {
        // email을 기반으로 Firebase에서 유저 정보를 가져오는 로직
        try {
            // Firebase 유저 정보 가져오기
            var userInfo = firebaseUserService.getUserInfoByEmail(email);
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Firebase 유저 정보를 찾을 수 없습니다.");
        }
    }
}