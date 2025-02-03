package com.example.bookingservice.repository;

import com.example.bookingservice.model.Hotel;
import com.example.bookingservice.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, String>, JpaSpecificationExecutor<Room>
        , PagingAndSortingRepository<Room, String> {
    Optional<Room> findByNumber(String roomNumber);

    Optional<Room> findByNumberAndHotel(String number, Hotel hotel);

}
