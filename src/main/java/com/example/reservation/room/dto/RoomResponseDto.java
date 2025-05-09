package com.example.reservation.room.dto;

import com.example.reservation.accommodation.dto.AccommodationSearchProjectionDto;
import com.example.reservation.room.domain.Room;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomResponseDto {
    private Long roomId;
    private Long accommodationId;
    private String name;
    private int capacity;
    private int priceWeekday;
    private int priceWeekend;

    public static RoomResponseDto from(Room room) {
        return RoomResponseDto.builder()
            .roomId(room.getId())
            .name(room.getName())
            .accommodationId(room.getAccommodation().getId())
            .capacity(room.getCapacity())
            .priceWeekday(room.getPriceWeekday())
            .priceWeekend(room.getPriceWeekend())
            .build();
    }

    public static RoomResponseDto fromProjection(AccommodationSearchProjectionDto dto) {
        return RoomResponseDto.builder()
            .roomId(dto.getRoomId())
            .accommodationId(dto.getAccommodationId())
            .name(dto.getRoomName())
            .capacity(dto.getCapacity())
            .build();
    }
}
