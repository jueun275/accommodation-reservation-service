package com.example.reservation.reservation.domain;

import com.example.reservation.common.jpa.BaseTimeEntity;
import com.example.reservation.room.domain.Room;
import com.example.reservation.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private LocalDateTime checkinDate;

    @Column(nullable = false)
    private LocalDateTime checkoutDate;

    @Column(nullable = false)
    private String guestCount;

    @Column(nullable = false)
    private int totalPrice;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @Builder
    public Reservation(User user,
                       Room room,
                       LocalDateTime checkinDate,
                       LocalDateTime checkoutDate,
                       String guestCount,
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
}
