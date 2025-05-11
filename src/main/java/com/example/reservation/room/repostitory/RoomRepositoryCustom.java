package com.example.reservation.room.repostitory;

import com.example.reservation.accommodation.dto.AccommodationSearchProjectionDto;
import com.example.reservation.accommodation.dto.AccommodationSearchRequestDto;
import java.util.List;

public interface RoomRepositoryCustom {

  List<AccommodationSearchProjectionDto> searchByConditions(
      AccommodationSearchRequestDto condition);
}
