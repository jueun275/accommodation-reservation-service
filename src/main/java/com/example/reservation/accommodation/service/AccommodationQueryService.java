package com.example.reservation.accommodation.service;

import com.example.reservation.accommodation.domain.Accommodation;
import com.example.reservation.accommodation.dto.AccommodationSearchRequestDto;
import com.example.reservation.accommodation.dto.AccommodationSearchResponseDto;
import com.example.reservation.room.domain.Room;
import com.example.reservation.room.repostitory.RoomRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccommodationQueryService {
    private final RoomRepository roomRepository;

    public List<AccommodationSearchResponseDto> searchAccommodations(AccommodationSearchRequestDto request) {
        List<Tuple> results = roomRepository.searchByConditions(request);

        Map<Accommodation, List<Room>> grouping = results.stream()
            .collect(Collectors.groupingBy(
                tuple -> tuple.get(1, Accommodation.class),
                Collectors.mapping(t -> t.get(0, Room.class), Collectors.toList())
            ));

        return grouping.entrySet().stream()
            .map(entry -> AccommodationSearchResponseDto.from(
                entry.getKey(),
                entry.getValue()
            )).collect(Collectors.toList());
    }
}
