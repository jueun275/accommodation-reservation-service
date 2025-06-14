package com.example.reservation.accommodation.dto;

import com.example.reservation.accommodation.domain.Accommodation;
import com.example.reservation.user.domain.User;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccommodationRequestDto {

  private Long ownerId;
  private String name;
  private String description;
  private String region;
  private String address;
  private LocalTime checkinTime;
  private LocalTime checkoutTime;

  public Accommodation toEntity(User owner) {
    return Accommodation.builder()
        .owner(owner)
        .name(name)
        .description(description)
        .region(region)
        .address(address)
        .checkinTime(checkinTime)
        .checkoutTime(checkoutTime)
        .build();
  }
}

