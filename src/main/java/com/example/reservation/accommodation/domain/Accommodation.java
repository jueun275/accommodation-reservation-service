package com.example.reservation.accommodation.domain;

import com.example.reservation.common.jpa.BaseTimeEntity;
import com.example.reservation.user.domain.User;
import jakarta.persistence.*;
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

    public void update(String name, String description, String region, String address) {
        this.name = name;
        this.description = description;
        this.region = region;
        this.address = address;
    }

    @Builder
    public Accommodation(Long id,
                         User owner,
                         String name,
                         String description,
                         String region,
                         String address
    ) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.region = region;
        this.address = address;
    }
}
