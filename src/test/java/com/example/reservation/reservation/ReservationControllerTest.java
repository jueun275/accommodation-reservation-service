package com.example.reservation.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.example.reservation.accommodation.domain.Accommodation;
import com.example.reservation.accommodation.domain.AccommodationRepository;
import com.example.reservation.payment.domain.Payment;
import com.example.reservation.payment.domain.PaymentRepository;
import com.example.reservation.reservation.domain.ReservationRepository;
import com.example.reservation.reservation.dto.ReservationRequestDto;
import com.example.reservation.room.domain.Room;
import com.example.reservation.room.repostitory.RoomRepository;
import com.example.reservation.user.domain.Role;
import com.example.reservation.user.domain.User;
import com.example.reservation.user.domain.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
class ReservationControllerTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AccommodationRepository accommodationRepository;

  @Autowired
  private RoomRepository roomRepository;

  @Autowired
  private ReservationRepository reservationRepository;

  @Autowired
  private PaymentRepository paymentRepository;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  private User user;
  private User owner;
  private Accommodation accommodation;
  private Room room1;

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
  }

  @AfterEach
  void tearDown() {
    paymentRepository.deleteAll();
    reservationRepository.deleteAll();
    roomRepository.deleteAll();
    accommodationRepository.deleteAll();
    userRepository.deleteAll();
  }


  @Test
  void 날짜가_겹치지_않는_요청이_동시에_들어오면_둘_다_성공한다() throws Exception {
    // given
    ReservationRequestDto request1 = new ReservationRequestDto(
        user.getId(), room1.getId(),
        LocalDate.of(2025, 4, 20),
        LocalDate.of(2025, 4, 22),
        2
    );

    ReservationRequestDto request2 = new ReservationRequestDto(
        user.getId(), room1.getId(),
        LocalDate.of(2025, 5, 21),
        LocalDate.of(2025, 5, 23),
        1
    );

    String json1 = objectMapper.writeValueAsString(request1);
    String json2 = objectMapper.writeValueAsString(request2);

    List<Integer> statusList = Collections.synchronizedList(new ArrayList<>());

    Runnable task1 = () -> {
      try {
        MvcResult result = mockMvc.perform(post("/api/reservation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json1))
            .andReturn();
        statusList.add(result.getResponse().getStatus());
      } catch (Exception e) {
        e.printStackTrace();
      }
    };

    Runnable task2 = () -> {
      try {
        MvcResult result = mockMvc.perform(post("/api/reservation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json2))
            .andReturn();
        statusList.add(result.getResponse().getStatus());
      } catch (Exception e) {
        e.printStackTrace();
      }
    };

    // when
    Thread t1 = new Thread(task1);
    Thread t2 = new Thread(task2);

    t1.start();
    t2.start();

    t1.join();
    t2.join();

    // then
    long reservationCount = reservationRepository.count();
    assertThat(reservationCount).isEqualTo(2);

    long successCount = statusList.stream().filter(s -> s == 200).count();
    long failCount = statusList.stream().filter(s -> s != 200).count();

    assertThat(successCount).isEqualTo(2);
    assertThat(failCount).isEqualTo(0);
  }


  @Test
  void 날짜가_겹치는_요청이_동시에_들어오면_하나만_성공한다() throws Exception {
    // given
    ReservationRequestDto request1 = new ReservationRequestDto(
        1L, 1L,
        LocalDate.of(2025, 5, 20),
        LocalDate.of(2025, 5, 22),
        2
    );

    ReservationRequestDto request2 = new ReservationRequestDto(
        2L, 1L, // 같은 방, 사용자만 다름
        LocalDate.of(2025, 5, 21),
        LocalDate.of(2025, 5, 23),
        1
    );

    String json1 = objectMapper.writeValueAsString(request1);
    String json2 = objectMapper.writeValueAsString(request2);

    List<Integer> statusList = Collections.synchronizedList(new ArrayList<>());

    Runnable task1 = () -> {
      try {
        MvcResult result = mockMvc.perform(post("/api/reservation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json1))
            .andReturn();
        statusList.add(result.getResponse().getStatus());
      } catch (Exception e) {
        // 예외 처리
        if (e.getCause() instanceof IllegalStateException) {
          statusList.add(400);
        } else {
          e.printStackTrace();
        }
      }
    };

    Runnable task2 = () -> {
      try {
        MvcResult result = mockMvc.perform(post("/api/reservation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json2))
            .andReturn();
        statusList.add(result.getResponse().getStatus());
      } catch (Exception e) {
        // 예외 처리
        if (e.getCause() instanceof IllegalStateException) {
          statusList.add(400);
        } else {
          e.printStackTrace();
        }
      }
    };

    // when
    Thread t1 = new Thread(task1);
    Thread t2 = new Thread(task2);

    t1.start();
    t2.start();

    t1.join();
    t2.join();

    // then
    long reservationCount = reservationRepository.count();
    assertThat(reservationCount).isEqualTo(1);

    long successCount = statusList.stream().filter(s -> s == 200).count();
    long failCount = statusList.stream().filter(s -> s != 200).count();

    assertThat(successCount).isEqualTo(1);
    assertThat(failCount).isEqualTo(1);
  }
}
