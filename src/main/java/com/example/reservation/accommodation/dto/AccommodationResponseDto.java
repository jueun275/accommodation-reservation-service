package com.example.reservation.accommodation.dto;

import com.example.reservation.accommodation.domain.Accommodation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccommodationResponseDto {
    private Long id;
    private Long ownerId;
    private String name;
    private String description;
    private String region;
    private String address;

    public static AccommodationResponseDto from(Accommodation accommodation) {
        return AccommodationResponseDto.builder()
            .id(accommodation.getId())
            .ownerId(accommodation.getOwner().getId())
            .name(accommodation.getName())
            .description(accommodation.getDescription())
            .region(accommodation.getRegion())
            .address(accommodation.getAddress())
            .build();
    }
}
