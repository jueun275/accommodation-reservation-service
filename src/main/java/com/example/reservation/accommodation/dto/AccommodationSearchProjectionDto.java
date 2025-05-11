package com.example.reservation.accommodation.dto;

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

  public AccommodationSearchProjectionDto(
      Long roomId, String roomName, int capacity, int priceWeekday, int priceWeekend,
      Long accommodationId, String accommodationName, String region
  ) {
    this.roomId = roomId;
    this.roomName = roomName;
    this.capacity = capacity;
    this.priceWeekday = priceWeekday;
    this.priceWeekend = priceWeekend;
    this.accommodationId = accommodationId;
    this.accommodationName = accommodationName;
    this.region = region;
  }
}
