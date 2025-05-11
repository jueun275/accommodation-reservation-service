package com.example.reservation.room.dto;

import com.example.reservation.accommodation.domain.Accommodation;
import com.example.reservation.room.domain.Room;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomRequestDto {

  private Long accommodationId;
  private String name;
  private int capacity;
  private int priceWeekday;
  private int priceWeekend;

  public Room toEntity(Accommodation accommodation) {
    return Room.builder()
        .accommodation(accommodation)
        .capacity(capacity)
        .name(name)
        .priceWeekday(priceWeekday)
        .priceWeekend(priceWeekend)
        .build();
  }
}
