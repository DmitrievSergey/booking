package com.example.statisticservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "booking_reservations")
public class BookingReservationEntity {
    @Id
    private String id;
    private String userId;
    private String roomId;

    private LocalDate startDate;
    private LocalDate endDate;

    public BookingReservationEntity(String userId, String roomId, LocalDate startDate, LocalDate endDate) {
        this.userId = userId;
        this.roomId = roomId;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
