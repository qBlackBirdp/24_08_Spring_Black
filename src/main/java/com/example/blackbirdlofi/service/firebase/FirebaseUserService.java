package com.example.blackbirdlofi.service.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import org.springframework.stereotype.Service;

@Service
public class FirebaseUserService {

    public UserRecord getUserInfoByEmail(String email) throws Exception {
        return FirebaseAuth.getInstance().getUserByEmail(email);
    }
}
