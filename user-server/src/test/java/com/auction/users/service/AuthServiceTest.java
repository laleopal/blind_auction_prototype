package com.auction.users.service;

import com.auction.users.dto.AuthResponse;
import com.auction.users.model.User;
import com.auction.users.repository.UserRepository;
import jdk.jfr.Description;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    @Description("test successful user authentication")
    public void testAuthenticate_Success() {

        User user = new User("testUser", "password");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("mockedToken");

        AuthResponse response = authService.authenticate(user);

        assertEquals("mockedToken", response.getJwt());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findByUsername("testUser");
        verify(jwtService, times(1)).generateToken(user);
    }

    @Test
    @Description("test failed user authentication")
    public void testAuthenticate_AuthFailure() {

        User user = new User("testUser", "password");
        doThrow(new BadCredentialsException("Invalid credentials")).when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        assertThrows(BadCredentialsException.class, () -> authService.authenticate(user));
        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(userRepository);
        verifyNoInteractions(jwtService);
    }

    @Test
    @Description("test failed user authentication with no user in database")
    public void testAuthenticate_RepositoryFailure() {

        User user = new User("testUser", "password");
        when(userRepository.findByUsername("testUser"))
                .thenThrow(new NoSuchElementException("Repository exception"));

        assertThrows(NoSuchElementException.class, () -> authService.authenticate(user));
        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findByUsername("testUser");
        verifyNoInteractions(jwtService);
    }
}
