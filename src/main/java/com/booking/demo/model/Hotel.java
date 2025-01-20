package com.booking.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Entity
@NoArgsConstructor
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

    private Long gradeCount = 0L;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "hotel")
    @MapKey(name = "number")
    private Map<String,Room> roomMap = new HashMap<>();

    public void addRoom(Room room) {
        roomMap.put(room.getNumber(), room);
    }

    public void deleteRoom(Room room) {
        roomMap.remove(room.getNumber());
    }
}
