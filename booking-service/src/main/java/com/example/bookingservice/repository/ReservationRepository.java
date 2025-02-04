package com.example.bookingservice.repository;

import com.example.bookingservice.model.ReservationInterval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<ReservationInterval, String> {
    @Query(value =
            "select r2.* from room r " +
                    "left join reservation r2 on r.id = r2.room_id " +
                    "where r.id  = :roomId and " +
                    "((r2.start_date >= :startDate and r2.start_date <= :endDate) " +
                    "or (r2.end_date >= :startDate and r2.end_date <= :endDate) " +
                    "or (r2.start_date <= :startDate and r2.end_date >= :endDate))"
            ,nativeQuery = true)
    List<ReservationInterval> checkIntervalForRoom(String roomId, LocalDate startDate, LocalDate endDate);
}
