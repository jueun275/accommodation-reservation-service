package com.example.reservation.reservation;

import com.example.reservation.global.aop.ReservationLock;
import com.example.reservation.global.security.LoginUser;
import com.example.reservation.reservation.dto.ReservationRequestDto;
import com.example.reservation.reservation.dto.ReservationDetailResponseDto;
import com.example.reservation.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/reservation")
public class ReservationController {

  private final ReservationService reservationService;

  @PostMapping
  @ReservationLock
  public ResponseEntity<ReservationDetailResponseDto> createReservation(
      @RequestBody ReservationRequestDto requestDto , @LoginUser Long userId) {
    ReservationDetailResponseDto responseDto = reservationService.createReservation(requestDto, userId);
    return ResponseEntity.ok(responseDto);
  }

  @DeleteMapping("/{reservationId}")
  public ResponseEntity<Void> cancelReservation(@PathVariable Long reservationId, @LoginUser Long userId) {
    reservationService.cancelReservation(reservationId, userId);
    return ResponseEntity.noContent().build();
  }
}
