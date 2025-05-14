package com.example.reservation.reservation;

import com.example.reservation.reservation.dto.ReservationRequestDto;
import com.example.reservation.reservation.dto.ReservationResponseDto;
import com.example.reservation.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<ReservationResponseDto> createReservation(@RequestBody ReservationRequestDto requestDto) {
    ReservationResponseDto responseDto = reservationService.createReservation(requestDto);
    return ResponseEntity.ok(responseDto);
  }
}
