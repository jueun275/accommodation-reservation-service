package com.example.reservation.reservation.dto;

import com.example.reservation.reservation.domain.Reservation;
import com.example.reservation.reservation.domain.ReservationStatus;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationDetailResponseDto {

  private Long reservationId;
  private Long userId;
  private Long roomId;
  private LocalDateTime checkinDate;
  private LocalDateTime checkoutDate;
  private int guestCount;
  private int totalPrice;
  private ReservationStatus status;

  public static ReservationDetailResponseDto from(Reservation reservation, LocalTime checkinTime, LocalTime checkoutTime) {
    return ReservationDetailResponseDto.builder()
        .reservationId(reservation.getId())
        .userId(reservation.getUser().getId())
        .roomId(reservation.getRoom().getId())
        .checkinDate(reservation.getCheckinDate().atTime(checkinTime))
        .checkoutDate(reservation.getCheckoutDate().atTime(checkoutTime))
        .guestCount(reservation.getGuestCount())
        .totalPrice(reservation.getTotalPrice())
        .status(reservation.getStatus())
        .build();
  }
}
