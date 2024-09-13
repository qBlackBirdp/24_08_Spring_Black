package com.example.blackbirdlofi.controller.firebase;

import com.example.blackbirdlofi.service.firebase.FirebaseUserService;
import com.example.blackbirdlofi.vo.ResultData;
import com.google.firebase.auth.FirebaseAuthException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class FirebaseController {

    private final FirebaseUserService firebaseUserService;

    @Autowired
    public FirebaseController(FirebaseUserService firebaseUserService) {
        this.firebaseUserService = firebaseUserService;
    }

    @PostMapping("/firebaseUser")
    public ResponseEntity<ResultData<?>> authenticateFirebaseUser(@RequestBody Map<String, String> request) {
        String idToken = request.get("idToken");

        System.err.println("Received idToken: " + idToken);

        try {
            // Firebase에서 idToken을 검증하고, 유저 정보를 얻음
            String jwtToken = firebaseUserService.googleLogin(idToken);

            // 로그: JWT 토큰 생성 성공
            System.err.println("JWT Token created successfully: " + jwtToken);

            // ResultData를 사용해서 성공 응답 반환
            ResultData<String> resultData = ResultData.from("S-1", "로그인 성공", "token", jwtToken);
            return ResponseEntity.ok(resultData);
        } catch (FirebaseAuthException e) {
            // Firebase 인증 오류 로그
            System.err.println("Firebase authentication error: " + e.getMessage());

            // ResultData를 사용해서 실패 응답 반환 (Firebase 인증 실패)
            ResultData<String> resultData = ResultData.from("F-1", "Firebase 인증 오류: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resultData);
        } catch (Exception e) {

            // 서버 오류 로그
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace(System.err);

            // ResultData를 사용해서 실패 응답 반환 (서버 오류)
            ResultData<String> resultData = ResultData.from("F-2", "서버 오류: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultData);
        }
    }
}