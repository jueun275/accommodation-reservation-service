package com.example.reservation.room.repostitory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.reservation.accommodation.domain.Accommodation;
import com.example.reservation.accommodation.domain.AccommodationRepository;
import com.example.reservation.accommodation.dto.AccommodationSearchProjectionDto;
import com.example.reservation.accommodation.dto.AccommodationSearchRequestDto;
import com.example.reservation.reservation.domain.Reservation;
import com.example.reservation.reservation.domain.ReservationRepository;
import com.example.reservation.reservation.domain.ReservationStatus;
import com.example.reservation.room.domain.Room;
import com.example.reservation.user.domain.Role;
import com.example.reservation.user.domain.User;
import com.example.reservation.user.domain.UserRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class RoomRepositoryImplTest {
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
    private Room room1;
    private Room room2;

    @BeforeEach
    void setUp() {
        user = userRepository.save(User.builder()
            .username("testuser@example.com")
            .password("1234")
            .name("테스터")
            .phone("01012345678")
            .role(Role.USER)
            .build());

        owner = userRepository.save(User.builder()
            .username("testowner@example.com")
            .password("1234")
            .name("테스터")
            .phone("01012345678")
            .role(Role.OWNER)
            .build());

        Accommodation acc = accommodationRepository.save(Accommodation.builder()
            .owner(owner)
            .name("테스트 숙소")
            .region("서울")
            .address("서울시 강남구")
            .checkinTime(LocalTime.of(15, 0))
            .checkoutTime(LocalTime.of(10, 0))
            .build());

        room1 = roomRepository.save(Room.builder()
            .accommodation(acc)
            .name("101호")
            .capacity(2)
            .priceWeekday(100000)
            .priceWeekend(150000)
            .build());

        room2 = roomRepository.save(Room.builder()
            .accommodation(acc)
            .name("102호")
            .capacity(2)
            .priceWeekday(90000)
            .priceWeekend(140000)
            .build());

        // room1 예약 생성
        reservationRepository.save(Reservation.builder()
            .user(user)
            .room(room1)
            .checkinDate(LocalDate.of(2025, 5, 10))
            .checkoutDate(LocalDate.of(2025, 5, 12))
            .guestCount(2)
            .totalPrice(200000)
            .status(ReservationStatus.RESERVED)
            .build());
    }

    @Test
    void 조건에_맞는_예약가능한_객실조회_테스트() {
        // given
        LocalDate checkIn = LocalDate.of(2025, 5, 10);  // room1은 이 날 예약됨
        LocalDate checkOut = LocalDate.of(2025, 5, 12);
        int guestCount = 2;
        String region = "서울";

        AccommodationSearchRequestDto requestDto = AccommodationSearchRequestDto.builder()
            .region(region)
            .guestCount(guestCount)
            .checkinDate(checkIn)
            .checkoutDate(checkOut)
            .build();
        // when
        List<AccommodationSearchProjectionDto> result = roomRepository.searchByConditions(requestDto);

        // then
        assertEquals(1, result.size());
        assertEquals(room2.getId(), result.get(0).getRoomId());
    }

    @Test
    void 숙소가_존재하지_않는_조건으로_조회_테스트() {
        //given
        LocalDate checkIn = LocalDate.of(2025, 5, 10);
        LocalDate checkOut = LocalDate.of(2025, 5, 12);
        int guestCount = 2;
        String region = "부산"; // 숙소가 존재하지 않는 지역

        AccommodationSearchRequestDto requestDto = AccommodationSearchRequestDto.builder()
            .region(region)
            .guestCount(guestCount)
            .checkinDate(checkIn)
            .checkoutDate(checkOut)
            .build();

        //when
        List<AccommodationSearchProjectionDto> result = roomRepository.searchByConditions(requestDto);

        //then
        assertTrue(result.isEmpty());
    }

}