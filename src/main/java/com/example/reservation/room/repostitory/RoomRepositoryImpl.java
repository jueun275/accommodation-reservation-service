package com.example.reservation.room.repostitory;

import com.example.reservation.accommodation.domain.QAccommodation;
import com.example.reservation.reservation.domain.QReservation;
import com.example.reservation.room.domain.QRoom;
import com.example.reservation.accommodation.dto.AccommodationSearchRequestDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RoomRepositoryImpl implements RoomRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Tuple> searchByConditions(AccommodationSearchRequestDto requestDto) {
        QRoom room = QRoom.room;
        QAccommodation acc = QAccommodation.accommodation;
        QReservation res = QReservation.reservation;

        BooleanBuilder builder = new BooleanBuilder();

        if (requestDto.getRegion() != null) {
            builder.and(acc.region.eq(requestDto.getRegion()));
        }

        if (requestDto.getGuestCount() != null) {
            builder.and(room.capacity.goe(requestDto.getGuestCount()));
        }

        if (requestDto.getCheckinDate() != null && requestDto.getCheckoutDate() != null) {
            builder.and(
                room.id.notIn(
                    JPAExpressions.select(res.room.id)
                        .from(res)
                        .where(
                            res.checkinDate.lt(requestDto.getCheckoutDate().atStartOfDay())
                                .and(res.checkoutDate.gt(requestDto.getCheckinDate().atStartOfDay()))
                        )
                )
            );
        }

        return queryFactory
            .select(room, acc)
            .from(room)
            .join(room.accommodation, acc).fetchJoin()
            .where(builder)
            .fetch();
    }
}