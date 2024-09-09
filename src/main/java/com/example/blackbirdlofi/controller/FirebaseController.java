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
            UserRecord userRecord = firebaseUserService.getUserByEmail(email);
            return "User found: " + userRecord.getEmail();
        } catch (FirebaseAuthException e) {
            return "Error: " + e.getMessage();
        }
    }
}