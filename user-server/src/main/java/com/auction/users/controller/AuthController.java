package com.auction.users.controller;

import com.auction.users.dto.AuthResponse;
import com.auction.users.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthController {
    @PostMapping("/register")
    public AuthResponse registerUser(@RequestBody User user) {
        //logic for user registration
        return new AuthResponse();
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody User user) {
        //logic for user login
        return new AuthResponse();
    }
}
