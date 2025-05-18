package com.example.reservation.reservation.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.reservation.accommodation.domain.Accommodation;
import com.example.reservation.accommodation.domain.AccommodationRepository;
import com.example.reservation.reservation.domain.Reservation;
import com.example.reservation.reservation.domain.ReservationRepository;
import com.example.reservation.reservation.domain.ReservationStatus;
import com.example.reservation.reservation.dto.ReservationRequestDto;
import com.example.reservation.reservation.dto.ReservationDetailResponseDto;
import com.example.reservation.room.domain.Room;
import com.example.reservation.room.repostitory.RoomRepository;
import com.example.reservation.user.domain.Role;
import com.example.reservation.user.domain.User;
import com.example.reservation.user.domain.UserRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ReservationServiceTest {

  @Autowired
  private ReservationService reservationService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AccommodationRepository accommodationRepository;

  @Autowired
  private RoomRepository roomRepository;

  @Autowired
  private ReservationRepository reservationRepository;

  private User user;
  private User owner;
  private Accommodation accommodation;
  private Room room1;
  private Room room2;

  @BeforeEach
  void setUp() {
    user = userRepository.save(User.builder()
        .username("testuser@example.com")
        .password("1234")
        .name("테스터 유저")
        .phone("01012345678")
        .role(Role.USER)
        .build());

    owner = userRepository.save(User.builder()
        .username("testowner@example.com")
        .password("1234")
        .name("테스터 오너")
        .phone("01012345678")
        .role(Role.USER)
        .build());

    accommodation = accommodationRepository.save(Accommodation.builder()
        .owner(owner)
        .name("테스트 호텔")
        .region("서울")
        .address("서울시 강남구")
        .checkinTime(LocalTime.of(15, 0))
        .checkoutTime(LocalTime.of(10, 0))
        .build());

    room1 = roomRepository.save(Room.builder()
        .accommodation(accommodation)
        .name("101")
        .capacity(2)
        .priceWeekday(100000)
        .priceWeekend(150000)
        .build());

    room2 = roomRepository.save(Room.builder()
        .accommodation(accommodation)
        .name("102")
        .capacity(2)
        .priceWeekday(90000)
        .priceWeekend(140000)
        .build());

  }

  @Test
  void crateReservation_success() {
    // given
    LocalDate checkin = LocalDate.of(2025, 5, 16);
    LocalDate checkout = LocalDate.of(2025, 5, 19);

    ReservationRequestDto request = ReservationRequestDto.builder()
        .userId(user.getId())
        .roomId(room1.getId())
        .checkinDate(checkin)
        .checkoutDate(checkout)
        .guestCount(2)
        .build();

    // when
    ReservationDetailResponseDto response = reservationService.createReservation(request);

    // then
    assertTrue(reservationRepository.findById(response.getReservationId()).isPresent());

    assertNotNull(response.getReservationId());
    assertEquals(user.getId(), response.getUserId());
    assertEquals(room1.getId(), response.getRoomId());
    assertEquals(checkin, response.getCheckinDate().toLocalDate());
    assertEquals(checkout, response.getCheckoutDate().toLocalDate());
    assertEquals("RESERVED", response.getStatus().name());


    assertEquals(400_000, response.getTotalPrice());
  }

  @Test
  void createReservation_fail_checkin_isbefore_checkout() {
    // given
    LocalDate checkin = LocalDate.of(2025, 5, 20);
    LocalDate checkout = LocalDate.of(2025, 5, 18); // 체크인보다 이전

    ReservationRequestDto request = ReservationRequestDto.builder()
        .userId(user.getId())
        .roomId(room1.getId())
        .checkinDate(checkin)
        .checkoutDate(checkout)
        .guestCount(2)
        .build();

    // when then
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
        reservationService.createReservation(request)
    );
    assertEquals("체크아웃 날짜는 체크인 날짜 이후여야 합니다", exception.getMessage());
  }

  @Test
  void createReservation_fail_already_exists() {
    // given
    Reservation existing = Reservation.builder()
        .user(user)
        .room(room1)
        .checkinDate(LocalDate.of(2025, 5, 16))
        .checkoutDate(LocalDate.of(2025, 5, 18))
        .guestCount(2)
        .totalPrice(250_000)
        .status(ReservationStatus.RESERVED)
        .build();
    reservationRepository.save(existing);

    ReservationRequestDto request = ReservationRequestDto.builder()
        .userId(user.getId())
        .roomId(room1.getId())
        .checkinDate(LocalDate.of(2025, 5, 17)) // 기존의 예약과 겹침
        .checkoutDate(LocalDate.of(2025, 5, 19))
        .guestCount(2)
        .build();

    // when then
    IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
        reservationService.createReservation(request)
    );
    System.out.println(exception.getMessage());
    assertEquals("해당 날짜에 예약이 이미 존재합니다.", exception.getMessage());
  }

}