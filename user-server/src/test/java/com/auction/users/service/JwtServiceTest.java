package com.auction.users.service;

import com.auction.users.model.Role;
import com.auction.users.model.User;
import jdk.jfr.Description;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {
    @Mock
    private JwtEncoder jwtEncoder;

    @Test
    @Description("test successful token generation")
    void testGenerateToken() {
        JwtService jwtService = new JwtService(jwtEncoder);
        UserDetails userDetails = new User("testUser", "password", Role.SELLER);

        String expectedToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJVc2VyX3NlcnZl" +
                "ciIsImlhdCI6MTYyMDk0NjMwMCwiZXhwIjoxNjIwOTQ5OTAyLCJzdWIiOiJ0ZXN0VXNlciIsInNjb3" +
                "BlIjoidGVzdCByZWFkIn0.e4qC9NruR1RCQD8zBvOy8kwhFznZ4_Z4r8-X1fv69rw";

        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "HS256");
        headers.put("typ", "JWT");

        Map<String, Object> claims = new HashMap<>();
        claims.put("exp", Instant.now().plus(1, ChronoUnit.HOURS));
        claims.put("sub", userDetails.getUsername());
        claims.put("scope", "test read");

        when(jwtEncoder.encode(Mockito.any())).thenReturn(
                new Jwt(expectedToken,
                        Instant.now(),
                        Instant.now().plus(1, ChronoUnit.HOURS),
                        headers,
                        claims)
        );

        String token = jwtService.generateToken(userDetails);

        assertEquals(expectedToken, token);
    }
}
