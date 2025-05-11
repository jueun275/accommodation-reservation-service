package com.example.reservation.room.domain;

import com.example.reservation.accommodation.domain.Accommodation;
import com.example.reservation.common.jpa.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "accommodation_id", nullable = false)
  private Accommodation accommodation;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private int capacity;

  @Column(nullable = false)
  private int priceWeekday;

  @Column(nullable = false)
  private int priceWeekend;

  public void update(int capacity, int priceWeekday, int priceWeekend) {
    this.capacity = capacity;
    this.priceWeekday = priceWeekday;
    this.priceWeekend = priceWeekend;
  }

  @Builder
  public Room(Long id,
      Accommodation accommodation,
      String name,
      int capacity,
      int priceWeekday,
      int priceWeekend
  ) {
    this.id = id;
    this.accommodation = accommodation;
    this.name = name;
    this.capacity = capacity;
    this.priceWeekday = priceWeekday;
    this.priceWeekend = priceWeekend;
  }
}
