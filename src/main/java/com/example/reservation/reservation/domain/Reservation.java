package com.example.reservation.reservation.domain;

import com.example.reservation.common.jpa.BaseTimeEntity;
import com.example.reservation.room.domain.Room;
import com.example.reservation.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "room_id", nullable = false)
  private Room room;

  @Column(nullable = false)
  private LocalDate checkinDate;

  @Column(nullable = false)
  private LocalDate checkoutDate;

  @Column(nullable = false)
  private int guestCount;

  @Column(nullable = false)
  private int totalPrice;

  @Enumerated(EnumType.STRING)
  private ReservationStatus status;

  @Builder
  public Reservation(User user,
      Room room,
      LocalDate checkinDate,
      LocalDate checkoutDate,
      int guestCount,
      int totalPrice,
      ReservationStatus status
  ) {
    this.user = user;
    this.room = room;
    this.checkinDate = checkinDate;
    this.checkoutDate = checkoutDate;
    this.guestCount = guestCount;
    this.totalPrice = totalPrice;
    this.status = status;
  }

  public void cancel() {
    this.status = ReservationStatus.CANCELLED;
  }
}
