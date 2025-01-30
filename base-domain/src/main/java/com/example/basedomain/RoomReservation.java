package com.example.basedomain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomReservation {
    private String userId;
    private String roomId;

    private LocalDate startDate;
    private LocalDate endDate;
}
