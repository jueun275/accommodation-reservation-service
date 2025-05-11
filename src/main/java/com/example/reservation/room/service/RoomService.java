package com.example.reservation.room.service;

import com.example.reservation.accommodation.domain.Accommodation;
import com.example.reservation.accommodation.domain.AccommodationRepository;
import com.example.reservation.room.domain.Room;
import com.example.reservation.room.repostitory.RoomRepository;
import com.example.reservation.room.dto.RoomRequestDto;
import com.example.reservation.room.dto.RoomResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {

  private final RoomRepository roomRepository;
  private final AccommodationRepository accommodationRepository;

  @Transactional
  public Long registerRoom(RoomRequestDto dto) {
    Accommodation accommodation = accommodationRepository.findById(dto.getAccommodationId())
        .orElseThrow(() -> new IllegalArgumentException("숙소가 존재하지 않습니다."));
    Room room = dto.toEntity(accommodation);
    return roomRepository.save(room).getId();
  }

  @Transactional
  public List<Long> registerRooms(List<RoomRequestDto> roomRequestDos) {
    Long accommodationId = roomRequestDos.get(0).getAccommodationId();
    Accommodation accommodation = accommodationRepository.findById(accommodationId)
        .orElseThrow(() -> new IllegalArgumentException("숙소가 존재하지 않습니다."));

    List<Room> rooms = roomRequestDos.stream()
        .map(dto -> dto.toEntity(accommodation))
        .collect(Collectors.toList());

    return roomRepository.saveAll(rooms).stream()
        .map(Room::getId)
        .collect(Collectors.toList());
  }

  @Transactional
  public void updateRoom(Long id, RoomRequestDto dto) {
    Room room = roomRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("방이 존재하지 않습니다."));
    room.update(dto.getCapacity(), dto.getPriceWeekday(), dto.getPriceWeekend());
  }

  @Transactional(readOnly = true)
  public RoomResponseDto getRoom(Long id) {
    Room room = roomRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("방이 존재하지 않습니다."));
    return RoomResponseDto.from(room);
  }

  @Transactional
  public void deleteRoom(Long id) {
    roomRepository.deleteById(id);
  }
}
