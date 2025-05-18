package com.example.reservation.reservation.dto;

import com.example.reservation.global.aop.ReservationLockIdInterface;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRequestDto implements ReservationLockIdInterface {

  private Long userId;
  private Long roomId;
  private LocalDate checkinDate;
  private LocalDate checkoutDate;
  private int guestCount;

  @Override
  public List<String> getLockKeys() {
    YearMonth checkin = YearMonth.from(checkinDate);
    YearMonth checkout = YearMonth.from(checkoutDate);

    long monthCount = ChronoUnit.MONTHS.between(checkin, checkout) + 1;

    return LongStream.range(0, monthCount)
        .mapToObj(checkin::plusMonths)
        .map(yearMonth -> String.format("lock:reservation:room%d:month:%d-%02d", roomId, yearMonth.getYear(),
            yearMonth.getMonthValue()))
        .collect(Collectors.toList());
  }
}
