package com.example.reservation.accommodation;

import com.example.reservation.accommodation.dto.AccommodationRequestDto;
import com.example.reservation.accommodation.dto.AccommodationResponseDto;
import com.example.reservation.accommodation.dto.AccommodationSearchRequestDto;
import com.example.reservation.accommodation.dto.AccommodationSearchResponseDto;
import com.example.reservation.accommodation.service.AccommodationQueryService;
import com.example.reservation.accommodation.service.AccommodationService;
import com.example.reservation.global.security.LoginUser;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accommodations")
@RequiredArgsConstructor
public class AccommodationController {

  private final AccommodationService accommodationService;
  private final AccommodationQueryService accommodationQueryService;

  // 숙소 등록
  @PreAuthorize("hasRole('OWNER')")
  @PostMapping
  public ResponseEntity<Void> registerAccommodation(
      @RequestBody AccommodationRequestDto requestDto, @LoginUser Long ownerId) {
    Long id = accommodationService.registerAccommodation(requestDto, ownerId);
    URI location = URI.create("/api/accommodations/" + id);
    return ResponseEntity.created(location).build();
  }

  // 숙소 수정
  @PreAuthorize("hasRole('OWNER')")
  @PutMapping("/{id}")
  public ResponseEntity<Void> updateAccommodation(@PathVariable Long id,
      @RequestBody AccommodationRequestDto requestDto, @LoginUser Long ownerId) {
    accommodationService.updateAccommodation(id, requestDto, ownerId);
    return ResponseEntity.noContent().build();
  }

  // 숙소 조회 (단건)
  @GetMapping("/{id}")
  public ResponseEntity<AccommodationResponseDto> getAccommodation(@PathVariable Long id) {
    AccommodationResponseDto dto = accommodationService.getAccommodation(id);
    return ResponseEntity.ok(dto);
  }

  // 예약 가능한 숙소 조회
  @GetMapping("/search")
  public ResponseEntity<List<AccommodationSearchResponseDto>> searchAccommodations(
      @RequestBody AccommodationSearchRequestDto requestDto) {
    return ResponseEntity.ok(accommodationQueryService.searchAccommodations(requestDto));
  }

  // 숙소 삭제
  @PreAuthorize("hasRole('OWNER')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteAccommodation(@PathVariable Long id, @LoginUser Long ownerId) {
    accommodationService.deleteAccommodation(id, ownerId);
    return ResponseEntity.noContent().build();
  }
}
