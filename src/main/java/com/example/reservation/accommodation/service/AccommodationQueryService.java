package com.example.reservation.accommodation.service;

import com.example.reservation.accommodation.domain.Accommodation;
import com.example.reservation.accommodation.dto.AccommodationSearchProjectionDto;
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
    List<AccommodationSearchProjectionDto> flatList = roomRepository.searchByConditions(request);

    Map<Long, List<AccommodationSearchProjectionDto>> groupedByAccommodation =
        flatList.stream().collect(Collectors.groupingBy(AccommodationSearchProjectionDto::getAccommodationId));

    return groupedByAccommodation.values().stream()
        .map(roomList -> {
          AccommodationSearchProjectionDto rep = roomList.get(0); // 대표값: 숙소 정보는 동일하므로 하나만 사용
          return AccommodationSearchResponseDto.fromProjectionGroup(rep, roomList);
        })
        .collect(Collectors.toList());
  }
}
