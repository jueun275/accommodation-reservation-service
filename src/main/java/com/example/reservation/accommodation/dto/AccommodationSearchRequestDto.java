package com.example.reservation.accommodation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccommodationSearchRequestDto {
    private String region;
    private Integer guestCount;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;

//    // 일단보류
//    private Integer minPrice;
//    private Integer maxPrice;
}
