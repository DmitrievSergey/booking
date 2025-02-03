package com.example.bookingservice.service;



import com.example.bookingservice.dto.filter.RoomFilter;
import com.example.bookingservice.model.Room;

import java.util.List;

public interface RoomService {
    Room save(Room room);

    Room update(String roomId, Room room);

    Room findRoomById(String roomId);

    void deleteRoomById(String roomId);

    List<Room> filterBy(RoomFilter filter);

}
