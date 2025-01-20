package com.booking.demo.repository;

import com.booking.demo.model.Hotel;
import com.booking.demo.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, String> {
    Optional<Room> findByNumber(String roomNumber);

    Optional<Room> findByNumberAndHotel(String number, Hotel hotel);

}
