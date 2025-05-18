package com.example.reservation.auth;

import com.example.reservation.global.security.TokenProvider;
import com.example.reservation.user.domain.User;
import com.example.reservation.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder;
  private final TokenProvider tokenProvider;

  public LoginResponse login(LoginRequest request) {
    User user = userRepository.findByUsername(request.getUsername())
        .orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다."));

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }

    String token = tokenProvider.generateToken(user.getId(), user.getUsername(), user.getRole());

    return LoginResponse.builder()
        .token(token)
        .username(user.getUsername())
        .role(user.getRole().name())
        .build();
  }
}
