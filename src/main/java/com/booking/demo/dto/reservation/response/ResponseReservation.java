package com.booking.demo.dto.reservation.response;

import com.booking.demo.model.Room;
import com.booking.demo.model.User;
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
