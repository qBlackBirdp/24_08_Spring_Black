package com.example.blackbirdlofi.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
public class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    public void testCreateToken() {
        String email = "test@example.com";
        String token = jwtTokenProvider.createToken(email);
        assertNotNull(token);
        System.out.println("Generated JWT Token: " + token);
    }

    @Test
    public void testValidateToken() {
        String email = "test@example.com";
        String token = jwtTokenProvider.createToken(email);
        boolean isValid = jwtTokenProvider.validateToken(token);
        assertTrue(isValid);
    }

    @Test
    public void testGetUserEmailFromToken() {
        String email = "test@example.com";
        String token = jwtTokenProvider.createToken(email);
        String extractedEmail = jwtTokenProvider.getUserEmailFromToken(token);
        assertEquals(email, extractedEmail);
    }
}
