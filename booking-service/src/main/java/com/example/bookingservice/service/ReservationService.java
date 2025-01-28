package com.example.bookingservice.service;



import com.example.bookingservice.model.ReservationInterval;

import java.util.List;

public interface ReservationService {
    ReservationInterval save(ReservationInterval reservationInterval);

    List<ReservationInterval> findAllReservations();

}
