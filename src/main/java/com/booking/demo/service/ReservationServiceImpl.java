package com.booking.demo.service;

import com.booking.demo.model.ReservationInterval;
import com.booking.demo.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService{
    private final ReservationRepository reservationRepository;

    @Override
    public ReservationInterval save(ReservationInterval reservationInterval) {
        return reservationRepository.saveAndFlush(reservationInterval);
    }

    @Override
    public List<ReservationInterval> findAllReservations() {
        return reservationRepository.findAll();
    }
}
