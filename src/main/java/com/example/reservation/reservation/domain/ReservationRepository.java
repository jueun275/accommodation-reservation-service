package com.example.reservation.reservation.domain;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

  boolean existsByRoomIdAndCheckinDateLessThanAndCheckoutDateGreaterThan(Long id,
      LocalDate checkoutDate, LocalDate checkinDate);

  List<Reservation> findAllByUserId(Long userId);
}
