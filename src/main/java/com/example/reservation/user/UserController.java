package com.example.reservation.user;

import com.example.reservation.global.security.LoginUser;
import com.example.reservation.user.dto.UserResponse;
import com.example.reservation.user.dto.UserSignUpRequest;
import com.example.reservation.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

  private final UserService userService;

  // 회원가입
  @PostMapping("/signup")
  public ResponseEntity<Long> signUp(@RequestBody UserSignUpRequest request) {
    Long userId =  userService.signUp(request);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(userId);
  }

  @GetMapping
  public ResponseEntity<UserResponse> getUserInfo(@LoginUser Long userId) {
    UserResponse userResponse= userService.getUserInfo(userId);
    return ResponseEntity.ok(userResponse);
  }

}
