package com.auction.auction.service;

import jdk.jfr.Description;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @Mock
    private JwtDecoder jwtDecoder;

    @InjectMocks
    private JwtService jwtService;

    @Test
    @Description("test isTokenValid with valid token")
    public void testIsTokenValid_True() {
        String token = "valid_token";
        when(jwtDecoder.decode(token)).thenReturn(mock(Jwt.class));

        boolean isValid = jwtService.isTokenValid(token);

        assertTrue(isValid);
    }

    @Test
    @Description("test isTokenValid with invalid token")
    public void testIsTokenValid_InvalidToken_False() {
        String token = "invalid_token";
        when(jwtDecoder.decode(token)).thenThrow(RuntimeException.class);

        boolean isValid = jwtService.isTokenValid(token);

        assertFalse(isValid);
    }

    @Test
    @Description("test roles extraction from jwt")
    public void testExtractRoles() {
        String token = "valid_token";
        Jwt jwt = mock(Jwt.class);
        when(jwtDecoder.decode(token)).thenReturn(jwt);
        when(jwt.getClaims()).thenReturn(Collections.singletonMap("scope", "SELLER"));

        List<SimpleGrantedAuthority> roles = jwtService.extractRoles(token);

        assertEquals(1, roles.size());
        assertTrue(roles.contains(new SimpleGrantedAuthority("SELLER")));
    }

    @Test
    public void testExtractSubject() {
        String token = "valid_token";
        Jwt jwt = mock(Jwt.class);
        when(jwtDecoder.decode(token)).thenReturn(jwt);
        when(jwt.getClaims()).thenReturn(Collections.singletonMap("sub", "testUser"));

        String subject = jwtService.extractSubject(token);

        assertEquals("testUser", subject);
    }
}
