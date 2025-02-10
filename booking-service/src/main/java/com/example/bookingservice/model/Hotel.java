package com.example.bookingservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Entity
@Table(name = "hotel", indexes = {
        @Index(name = "ntt_index", columnList = "name, address, town", unique = true)})
@NoArgsConstructor
@AllArgsConstructor
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private String title;

    private String town;

    private String address;

    private String distance;

    private Float rating = 0.0f;
    @Column(name = "number_of_rating")
    private int numberOfRating;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "hotel")
    @MapKey(name = "number")
    private Map<String,Room> roomMap = new HashMap<>();

    public void addRoom(Room room) {
        roomMap.put(room.getNumber(), room);
    }

    public void deleteRoom(Room room) {
        roomMap.remove(room.getNumber());
    }

    public Hotel(String name, String title, String town, String address, String distance, Float rating, int numberOfRating) {
        this.name = name;
        this.title = title;
        this.town = town;
        this.address = address;
        this.distance = distance;
        this.rating = rating;
        this.numberOfRating = numberOfRating;
    }

    public Hotel(String id, String name, String title, String town, String address, String distance, Float rating, int numberOfRating) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.town = town;
        this.address = address;
        this.distance = distance;
        this.rating = rating;
        this.numberOfRating = numberOfRating;
    }
}
