package com.example.bookingservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Room implements Serializable {
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
    @JsonIgnore(value = true)
    private Hotel hotel;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "room")
    private List<ReservationInterval> intervalList = new ArrayList<>();

    public void addInterval(ReservationInterval interval) {
        intervalList.add(interval);
    }

    public Room(String id, String name, String description, String number, Float pricePerDay, Byte peopleCount, Hotel hotel) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.number = number;
        this.pricePerDay = pricePerDay;
        this.peopleCount = peopleCount;
        this.hotel = hotel;
    }
}
