package com.booking.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private String description;

    private String number;

    @Column(name = "price")
    private Float pricePerDay;

    @Column(name = "people_count")
    private Byte peopleCount;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "room")
    private List<ReservationInterval> intervalList = new ArrayList<>();

    public void addInterval(ReservationInterval interval) {
        intervalList.add(interval);
    }
}
