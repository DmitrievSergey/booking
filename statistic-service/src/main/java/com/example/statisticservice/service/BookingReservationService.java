package com.example.statisticservice.service;

import com.example.statisticservice.entity.BookingReservationEntity;

import java.util.List;

public interface BookingReservationService {
    void save(BookingReservationEntity reservation);

    List<BookingReservationEntity> findAll();
}
