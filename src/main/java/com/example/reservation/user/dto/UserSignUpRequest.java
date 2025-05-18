package com.example.reservation.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpRequest {
  private String username;
  private String password;
  private String name;
  private String phoneNumber;
  private String role;
}
