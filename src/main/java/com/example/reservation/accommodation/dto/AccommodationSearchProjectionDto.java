package com.example.reservation.accommodation.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;

@Getter
public class AccommodationSearchProjectionDto {

  private final Long roomId;
  private final String roomName;
  private final int capacity;
  private final int priceWeekday;
  private final int priceWeekend;
  private final Long accommodationId;
  private final String accommodationName;
  private final String region;
  private final LocalTime checkinTime;
  private final LocalTime checkoutTime;

  public AccommodationSearchProjectionDto(
      Long roomId, String roomName, int capacity, int priceWeekday, int priceWeekend,
      Long accommodationId, String accommodationName, String region,
      LocalTime checkinTime, LocalTime checkoutTime
  ) {
    this.roomId = roomId;
    this.roomName = roomName;
    this.capacity = capacity;
    this.priceWeekday = priceWeekday;
    this.priceWeekend = priceWeekend;
    this.accommodationId = accommodationId;
    this.accommodationName = accommodationName;
    this.region = region;
    this.checkinTime = checkinTime;
    this.checkoutTime = checkoutTime;
  }
}
