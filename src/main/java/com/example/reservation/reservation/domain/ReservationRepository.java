package com.example.reservation.reservation.domain;

import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

  boolean existsByRoomIdAndCheckinDateLessThanAndCheckoutDateGreaterThan(Long id,
      LocalDate checkoutDate, LocalDate checkinDate);
}
