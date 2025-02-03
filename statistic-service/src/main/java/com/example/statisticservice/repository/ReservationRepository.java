package com.example.statisticservice.repository;

import com.example.statisticservice.entity.BookingReservationEntity;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ReservationRepository extends MongoRepository<BookingReservationEntity, String> {
}
