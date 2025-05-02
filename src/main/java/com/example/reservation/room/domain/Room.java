package com.example.reservation.room.domain;

import com.example.reservation.common.jpa.BaseTimeEntity;
import com.example.reservation.accommodation.domain.Accommodation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room  extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accommodation_id", nullable = false)
    private Accommodation accommodation;

    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false)
    private int priceWeekday;

    @Column(nullable = false)
    private int priceWeekend;

    @Builder
    public Room(Long id, Accommodation accommodation, int capacity, int priceWeekday, int priceWeekend) {
        this.id = id;
        this.accommodation = accommodation;
        this.capacity = capacity;
        this.priceWeekday = priceWeekday;
        this.priceWeekend = priceWeekend;
    }
}
