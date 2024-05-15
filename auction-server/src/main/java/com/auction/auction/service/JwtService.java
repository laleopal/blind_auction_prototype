package com.auction.auction.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtDecoder jwtDecoder;

    public boolean isTokenValid(String token) {
        try {
            decode(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<SimpleGrantedAuthority> extractRoles(String token) {
        return Arrays.stream(extractClaims(token).get("scope").toString().split(" "))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
    }

    public Jwt decode(String jwt) {
        try {return jwtDecoder.decode(jwt);}
        catch (Exception e) {
            throw e;
        }

    }

    public String extractSubject(String token) {
        return extractClaims(token).get("sub").toString();
    }

    private Map<String, Object> extractClaims(String token) {
        return decode(token).getClaims();
    }

}
