package com.example.bookingservice.dto.reservation.response;


import com.example.bookingservice.model.Room;
import com.example.bookingservice.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseReservation {

    private LocalDate startDate;

    private LocalDate endDate;

    private Room room;

    private User user;
}
