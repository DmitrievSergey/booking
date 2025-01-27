package com.booking.demo.repository;

import com.booking.demo.model.Hotel;
import com.booking.demo.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, String>, JpaSpecificationExecutor<Room>
        , PagingAndSortingRepository<Room, String> {
    Optional<Room> findByNumber(String roomNumber);

    Optional<Room> findByNumberAndHotel(String number, Hotel hotel);

}
