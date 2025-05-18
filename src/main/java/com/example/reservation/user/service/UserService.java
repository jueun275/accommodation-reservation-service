package com.example.reservation.user.service;

import com.example.reservation.reservation.domain.ReservationRepository;
import com.example.reservation.user.domain.Role;
import com.example.reservation.user.domain.User;
import com.example.reservation.user.domain.UserRepository;
import com.example.reservation.user.dto.UserSignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final ReservationRepository reservationRepository;
  private final BCryptPasswordEncoder passwordEncoder;

  // 회원 가입
  @Transactional
  public Long signUp(UserSignUpRequest request) {

    if (userRepository.existsByUsername(request.getUsername())) {
      throw new IllegalArgumentException("해당 이메일은 이미 가입된 이메일입니다.");
    }

    String encodedPassword = passwordEncoder.encode(request.getPassword());

    Role role = Role.valueOf(request.getRole().toUpperCase());

    User user = User.builder()
        .username(request.getUsername())
        .password(encodedPassword)
        .phone(request.getPhoneNumber())
        .name(request.getName())
        .role(role)
        .build();

    return userRepository.save(user).getId();
  }

}
