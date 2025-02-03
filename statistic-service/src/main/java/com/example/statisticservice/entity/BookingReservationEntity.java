package com.example.statisticservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(id).append('|');
        sb.append(userId).append('|');
        sb.append(roomId).append('|');
        sb.append(startDate).append('|');
        sb.append(endDate);
        sb.append("\n");
        return sb.toString();
    }
}
