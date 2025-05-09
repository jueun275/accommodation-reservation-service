package com.example.reservation.accommodation.service;

import com.example.reservation.accommodation.domain.Accommodation;
import com.example.reservation.accommodation.domain.AccommodationRepository;
import com.example.reservation.accommodation.dto.AccommodationSearchRequestDto;
import com.example.reservation.accommodation.dto.AccommodationSearchResponseDto;
import com.example.reservation.reservation.domain.Reservation;
import com.example.reservation.reservation.domain.ReservationRepository;
import com.example.reservation.reservation.domain.ReservationStatus;
import com.example.reservation.room.domain.Room;
import com.example.reservation.room.repostitory.RoomRepository;
import com.example.reservation.user.domain.Role;
import com.example.reservation.user.domain.User;
import com.example.reservation.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AccommodationQueryServiceTest {

    @Autowired
    private AccommodationQueryService queryService;

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
            .email("testuser@example.com")
            .password("1234")
            .name("테스터 유저")
            .phone("01012345678")
            .role(Role.USER)
            .build());

        owner = userRepository.save(User.builder()
            .email("testowner@example.com")
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

        reservationRepository.save(Reservation.builder()
            .user(user)
            .room(room1)
            .checkinDate(LocalDateTime.of(2025, 5, 10, 14, 0))
            .checkoutDate(LocalDateTime.of(2025, 5, 12, 11, 0))
            .guestCount("2")
            .totalPrice(200000)
            .status(ReservationStatus.RESERVED)
            .build());
    }

    @Test
    void 조건에_맞는_예약가능한_객실조회_및_dto_변환() {
        // given
        AccommodationSearchRequestDto requestDto = AccommodationSearchRequestDto.builder()
            .region("서울")
            .guestCount(2)
            .checkinDate(LocalDate.of(2025, 5, 10))
            .checkoutDate(LocalDate.of(2025, 5, 12))
            .build();

        // when
        List<AccommodationSearchResponseDto> result = queryService.searchAccommodations(requestDto);

        // then
        assertEquals(1, result.size());

        AccommodationSearchResponseDto dto = result.get(0);
        assertEquals(accommodation.getId(), dto.getAccommodationId());
        assertEquals(1, dto.getRoomInfo().size());
        assertEquals(room2.getId(), dto.getRoomInfo().get(0).getRoomId());
    }
}