package com.example.reservation.accommodation;

import com.example.reservation.accommodation.dto.AccommodationRequestDto;
import com.example.reservation.accommodation.dto.AccommodationResponseDto;
import com.example.reservation.accommodation.dto.AccommodationSearchRequestDto;
import com.example.reservation.accommodation.dto.AccommodationSearchResponseDto;
import com.example.reservation.accommodation.service.AccommodationQueryService;
import com.example.reservation.accommodation.service.AccommodationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/accommodations")
@RequiredArgsConstructor
public class AccommodationController {

    private final AccommodationService accommodationService;
    private final AccommodationQueryService accommodationQueryService;

    // 숙소 등록
    @PostMapping
    public ResponseEntity<Void> registerAccommodation(@RequestBody AccommodationRequestDto requestDto) {
        Long id = accommodationService.registerAccommodation(requestDto);
        URI location = URI.create("/api/accommodations/" + id);
        return ResponseEntity.created(location).build();
    }

    // 숙소 수정
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAccommodation(@PathVariable Long id,
                                                    @RequestBody AccommodationRequestDto requestDto) {
        accommodationService.updateAccommodation(id, requestDto);
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
    public ResponseEntity<List<AccommodationSearchResponseDto>> searchAccommodations(@RequestBody AccommodationSearchRequestDto requestDto) {
        return ResponseEntity.ok(accommodationQueryService.searchAccommodations(requestDto));
    }

    // 숙소 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccommodation(@PathVariable Long id) {
        accommodationService.deleteAccommodation(id);
        return ResponseEntity.noContent().build();
    }
}
