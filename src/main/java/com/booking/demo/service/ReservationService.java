package com.booking.demo.service;

import com.booking.demo.model.ReservationInterval;

import java.util.List;

public interface ReservationService {
    ReservationInterval save(ReservationInterval reservationInterval);

    List<ReservationInterval> findAllReservations();

}
