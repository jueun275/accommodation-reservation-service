package com.example.reservation.accommodation.service;

import com.example.reservation.accommodation.domain.Accommodation;
import com.example.reservation.accommodation.domain.AccommodationRepository;
import com.example.reservation.accommodation.dto.AccommodationRequestDto;
import com.example.reservation.accommodation.dto.AccommodationResponseDto;
import com.example.reservation.user.domain.User;
import com.example.reservation.user.domain.UserRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccommodationService {

  private final AccommodationRepository accommodationRepository;
  private final UserRepository userRepository;

  @Transactional
  public Long registerAccommodation(AccommodationRequestDto dto, Long ownerId) {
    User owner = userRepository.findById(ownerId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    Accommodation accommodation = dto.toEntity(owner);
    return accommodationRepository.save(accommodation).getId();
  }

  @Transactional
  public void updateAccommodation(Long id, AccommodationRequestDto dto, Long ownerId) {
    Accommodation accommodation = accommodationRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 숙소가 존재하지 않습니다."));
    if(!Objects.equals(ownerId, accommodation.getOwner().getId())) {
      throw new IllegalArgumentException("사용자가 관리하는 업체가 아닙니다");
    }
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
  public void deleteAccommodation(Long id, Long ownerId) {
    Accommodation accommodation = accommodationRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 숙소가 존재하지 않습니다."));
    if(!Objects.equals(ownerId, accommodation.getOwner().getId())) {
      throw new IllegalArgumentException("사용자가 관리하는 업체가 아닙니다");
    }
    accommodationRepository.deleteById(id);
  }
}
