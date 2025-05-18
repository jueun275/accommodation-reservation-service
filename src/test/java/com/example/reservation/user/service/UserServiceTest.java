package com.example.reservation.user.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.reservation.user.domain.UserRepository;
import com.example.reservation.user.dto.UserSignUpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTest {

  @Autowired
  private UserService userService;

  @Autowired
  private UserRepository userRepository;

  @Test
  void registerUser() {
    // given
    UserSignUpRequest request = UserSignUpRequest.builder()
        .phoneNumber("123456789")
        .name("testUser")
        .role("USER")
        .password("password")
        .username("testUser@naver.com")
        .build();

    // when
    Long responseUserId = userService.signUp(request);

    // then
    assertTrue(userRepository.findById(responseUserId).isPresent());
  }

}