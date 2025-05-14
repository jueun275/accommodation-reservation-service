package com.example.reservation.accommodation.domain;

import com.example.reservation.common.jpa.BaseTimeEntity;
import com.example.reservation.user.domain.User;
import jakarta.persistence.*;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Accommodation extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "owner_id", nullable = false)
  private User owner;

  @Column(nullable = false)
  private String name;

  private String description;

  @Column(nullable = false)
  private String region;

  @Column(nullable = false)
  private String address;

  @Column(nullable = false)
  private LocalTime checkinTime;

  @Column(nullable = false)
  private LocalTime checkoutTime;

  public void update(
      String name,
      String description,
      String region,
      String address,
      LocalTime checkinTime,
      LocalTime checkoutTime
  ) {
    this.name = name;
    this.description = description;
    this.region = region;
    this.address = address;
    this.checkinTime = checkinTime;
    this.checkoutTime = checkoutTime;
  }

  @Builder
  public Accommodation(Long id,
      User owner,
      String name,
      String description,
      String region,
      String address,
      LocalTime checkinTime,
      LocalTime checkoutTime
  ) {
    this.id = id;
    this.owner = owner;
    this.name = name;
    this.description = description;
    this.region = region;
    this.address = address;
    this.checkinTime = checkinTime;
    this.checkoutTime = checkoutTime;
  }
}
