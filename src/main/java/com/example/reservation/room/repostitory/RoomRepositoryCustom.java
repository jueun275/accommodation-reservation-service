package com.example.reservation.room.repostitory;

import com.example.reservation.accommodation.dto.AccommodationSearchRequestDto;
import com.querydsl.core.Tuple;

import java.util.List;

public interface RoomRepositoryCustom {
    List<Tuple> searchByConditions(AccommodationSearchRequestDto condition);
}
