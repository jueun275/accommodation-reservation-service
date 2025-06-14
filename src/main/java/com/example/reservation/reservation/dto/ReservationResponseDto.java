package com.example.reservation.reservation.dto;

import com.example.reservation.reservation.domain.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponseDto {
  private Long reservationId;
  private Long userId;
  private Long roomId;
  private int guestCount;
  private int totalPrice;

  public static ReservationResponseDto from(Reservation reservation) {
    return ReservationResponseDto.builder()
        .reservationId(reservation.getId())
        .userId(reservation.getUser().getId())
        .roomId(reservation.getRoom().getId())
        .guestCount(reservation.getGuestCount())
        .totalPrice(reservation.getTotalPrice())
        .build();
  }
}
