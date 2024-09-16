package com.example.blackbirdlofi.service.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import org.springframework.stereotype.Service;

@Service
public class FirebaseUserService {

    public boolean verifyToken(String idToken) {
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            System.out.println("Firebase JWT 검증 성공: " + decodedToken.getUid());
            return true;
        } catch (Exception e) {
            System.out.println("Firebase JWT 검증 실패: " + e.getMessage());
            return false;
        }
    }

    public UserRecord getUserInfoByEmail(String email) throws Exception {
        return FirebaseAuth.getInstance().getUserByEmail(email);
    }
}
