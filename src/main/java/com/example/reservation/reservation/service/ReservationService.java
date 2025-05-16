package com.example.reservation.reservation.service;

import com.example.reservation.accommodation.domain.Accommodation;
import com.example.reservation.global.aop.ReservationLock;
import com.example.reservation.payment.domain.Payment;
import com.example.reservation.payment.domain.PaymentRepository;
import com.example.reservation.payment.domain.PaymentState;
import com.example.reservation.reservation.domain.Reservation;
import com.example.reservation.reservation.domain.ReservationStatus;
import com.example.reservation.reservation.dto.ReservationRequestDto;
import com.example.reservation.reservation.domain.ReservationRepository;
import com.example.reservation.reservation.dto.ReservationResponseDto;
import com.example.reservation.room.domain.Room;
import com.example.reservation.room.repostitory.RoomRepository;
import com.example.reservation.user.domain.User;
import com.example.reservation.user.domain.UserRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

  private final UserRepository userRepository;
  private final RoomRepository roomRepository;
  private final ReservationRepository reservationRepository;
  private final PaymentRepository paymentRepository;

  @Transactional
  public ReservationResponseDto createReservation(ReservationRequestDto request) {
    User user = userRepository.findById(request.getUserId())
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

    Room room = roomRepository.findById(request.getRoomId())
        .orElseThrow(() -> new IllegalArgumentException("해당 방이 존재하지 않습니다."));

    Accommodation accommodation = room.getAccommodation();

    validateReservationDate(room.getId(), request.getCheckinDate(), request.getCheckoutDate());

    int totalPrice = calculateTotalPrice(room, request.getCheckinDate(), request.getCheckoutDate());

    Reservation reservation = reservationRepository.save(
        Reservation.builder()
            .user(user)
            .room(room)
            .checkinDate(request.getCheckinDate())
            .checkoutDate(request.getCheckoutDate())
            .guestCount(request.getGuestCount())
            .totalPrice(totalPrice)
            .status(ReservationStatus.RESERVED)
            .build()
    );

    // 결제 정보 저장
    paymentRepository.save(Payment.builder()
        .reservation(reservation)
        .amount(totalPrice)
        .state(PaymentState.PAID)
        .paymentAt(LocalDateTime.now())
        .build());


    return ReservationResponseDto.from(
        reservation,
        accommodation.getCheckinTime(),
        accommodation.getCheckoutTime()
    );
  }

  private int calculateTotalPrice(Room room, LocalDate checkin, LocalDate checkout) {
    int totalPrice = 0;

    while (checkin.isBefore(checkout)) {
      DayOfWeek day = checkin.getDayOfWeek();
      boolean isWeekend = (day == DayOfWeek.FRIDAY || day == DayOfWeek.SATURDAY); // 금,토 주말요금

      totalPrice += isWeekend ? room.getPriceWeekend() : room.getPriceWeekday();
      checkin = checkin.plusDays(1);
    }

    return totalPrice;
  }

  private void validateReservationDate(Long roomId, LocalDate checkin, LocalDate checkout) {
    if (!checkin.isBefore(checkout)) {
      throw new IllegalArgumentException("체크아웃 날짜는 체크인 날짜 이후여야 합니다");
    }

    boolean exists = reservationRepository.existsByRoomIdAndCheckinDateLessThanAndCheckoutDateGreaterThan(
        roomId, checkout, checkin);

    if (exists) {
      throw new IllegalStateException("해당 날짜에 예약이 이미 존재합니다.");
    }
  }
}
