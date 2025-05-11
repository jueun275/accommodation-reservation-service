package com.example.reservation.accommodation.dto;

import com.example.reservation.accommodation.domain.Accommodation;
import com.example.reservation.room.domain.Room;
import com.example.reservation.room.dto.RoomResponseDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccommodationSearchResponseDto {

  private Long accommodationId;
  private String name;
  private String region;
  private List<RoomResponseDto> roomInfo;

  public static AccommodationSearchResponseDto from(
      Accommodation accommodation,
      List<Room> rooms
  ) {
    return AccommodationSearchResponseDto.builder()
        .accommodationId(accommodation.getId())
        .name(accommodation.getName())
        .region(accommodation.getRegion())
        .roomInfo(rooms.stream()
            .map(RoomResponseDto::from)
            .collect(Collectors.toList()))
        .build();
  }

  public static AccommodationSearchResponseDto fromProjectionGroup(
      AccommodationSearchProjectionDto projectionDto,
      List<AccommodationSearchProjectionDto> rooms
  ) {
    return AccommodationSearchResponseDto.builder()
        .accommodationId(projectionDto.getAccommodationId())
        .name(projectionDto.getAccommodationName())
        .region(projectionDto.getRegion())
        .roomInfo(rooms.stream()
            .map(RoomResponseDto::fromProjection)
            .collect(Collectors.toList()))
        .build();
  }
}
