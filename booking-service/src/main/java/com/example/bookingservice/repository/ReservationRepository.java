package com.example.bookingservice.repository;

import com.example.bookingservice.model.ReservationInterval;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<ReservationInterval, String> {

}
