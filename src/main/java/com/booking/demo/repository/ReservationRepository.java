package com.booking.demo.repository;

import com.booking.demo.model.ReservationInterval;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<ReservationInterval, String> {

}
