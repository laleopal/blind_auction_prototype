package com.auction.users.service;

import com.auction.users.dto.AuthResponse;
import com.auction.users.model.User;
import com.auction.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthResponse authenticate(User user) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );

        User userFromDb = userRepository.findByUsername(user.getUsername()).orElseThrow();
        return new AuthResponse(jwtService.generateToken(userFromDb));
    }
}
