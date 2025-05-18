package com.example.reservation.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.example.reservation.global.security.TokenProvider;
import com.example.reservation.user.domain.Role;
import com.example.reservation.user.domain.User;
import com.example.reservation.user.domain.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private BCryptPasswordEncoder passwordEncoder;

  @Mock
  private TokenProvider tokenProvider;

  @InjectMocks
  private AuthService authService;


  @Test
  void login_success() {
    Long userId = 3L;
    String username = "test3@test.com";
    String rawPassword = "1234";
    String encodedPassword = "암호화된1234";

    User user = User.builder()
        .id(userId)
        .username(username)
        .password(encodedPassword)
        .role(Role.USER)
        .build();

    when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
    when(tokenProvider.generateToken(userId, username, Role.USER)).thenReturn("JWT-TOKEN");

    LoginResponse response = authService.login(new LoginRequest(username, rawPassword));

    assertEquals("JWT-TOKEN", response.getToken());
  }
}