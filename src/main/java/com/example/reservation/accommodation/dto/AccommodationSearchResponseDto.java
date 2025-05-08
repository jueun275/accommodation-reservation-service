package com.example.reservation.accommodation.dto;

import com.example.reservation.accommodation.domain.Accommodation;
import com.example.reservation.room.domain.Room;
import com.example.reservation.room.dto.RoomResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class AccommodationSearchResponseDto {
    private Long id;
    private String name;
    private String region;
    private List<RoomResponseDto> roomInfo;

    @Builder
    public AccommodationSearchResponseDto(
        Long id,
        String name,
        String region,
        List<RoomResponseDto> roomInfo
    ) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.roomInfo = roomInfo;
    }

    public static AccommodationSearchResponseDto from(
        Accommodation accommodation,
        List<Room> rooms
    ) {
        return AccommodationSearchResponseDto.builder()
            .id(accommodation.getId())
            .name(accommodation.getName())
            .region(accommodation.getRegion())
            .roomInfo(rooms.stream()
                .map(RoomResponseDto::from)
                .collect(Collectors.toList()))
            .build();
    }
}
