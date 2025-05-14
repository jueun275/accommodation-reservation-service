package com.example.reservation.accommodation.service;

import com.example.reservation.accommodation.domain.Accommodation;
import com.example.reservation.accommodation.domain.AccommodationRepository;
import com.example.reservation.accommodation.dto.AccommodationRequestDto;
import com.example.reservation.accommodation.dto.AccommodationResponseDto;
import com.example.reservation.user.domain.User;
import com.example.reservation.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccommodationService {

  private final AccommodationRepository accommodationRepository;
  private final UserRepository userRepository;

  @Transactional
  public Long registerAccommodation(AccommodationRequestDto dto) {
    User owner = userRepository.findById(dto.getOwnerId())
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    Accommodation accommodation = dto.toEntity(owner);
    return accommodationRepository.save(accommodation).getId();
  }

  @Transactional
  public void updateAccommodation(Long id, AccommodationRequestDto dto) {
    Accommodation accommodation = accommodationRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 숙소가 존재하지 않습니다."));
    accommodation.update(
        dto.getName(), dto.getDescription(), dto.getRegion(), dto.getAddress(), dto.getCheckinTime(), dto.getCheckoutTime());
  }

  @Transactional(readOnly = true)
  public AccommodationResponseDto getAccommodation(Long id) {
    Accommodation accommodation = accommodationRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 숙소가 존재하지 않습니다."));
    return AccommodationResponseDto.from(accommodation);
  }

  @Transactional
  public void deleteAccommodation(Long id) {
    accommodationRepository.deleteById(id);
  }
}
