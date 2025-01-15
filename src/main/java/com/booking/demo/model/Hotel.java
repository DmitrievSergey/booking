package com.booking.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String name;

    String title;

    String town;

    String address;

    String distance;

    Float rating = 0.0f;

    Long gradeCount = 0L;
}
