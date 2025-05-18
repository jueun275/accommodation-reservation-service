package com.example.reservation.user.dto;

import com.example.reservation.reservation.dto.ReservationResponseDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponse {

  private String username;
  private String name;
  private String phone;
  private List<ReservationResponseDto> reservations;

  public UserResponse(String username, String name, String phone,
      List<ReservationResponseDto> reservations) {
    this.username = username;
    this.name = name;
    this.phone = phone;
   if(reservations != null)  this.reservations = reservations;
  }
}

