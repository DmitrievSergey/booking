package com.example.statisticservice.service;

import com.example.statisticservice.entity.BookingReservationEntity;
import com.example.statisticservice.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingReservationServiceImpl implements BookingReservationService {
    private final ReservationRepository reservationRepository;

    @Override
    public void save(BookingReservationEntity reservation) {
        reservationRepository.save(reservation);
    }
}
