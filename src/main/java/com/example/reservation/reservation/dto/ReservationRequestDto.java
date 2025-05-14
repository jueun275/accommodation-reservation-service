package com.example.reservation.reservation.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRequestDto {

  private Long userId;
  private Long roomId;
  private LocalDate checkinDate;
  private LocalDate checkoutDate;
  private int guestCount;
}
