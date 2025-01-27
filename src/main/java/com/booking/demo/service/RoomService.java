package com.booking.demo.service;

import com.booking.demo.dto.filter.RoomFilter;
import com.booking.demo.model.Room;

import java.util.List;

public interface RoomService {
    Room save(Room room);

    Room update(String roomId, Room room);

    Room findRoomById(String roomId);

    void deleteRoomById(String roomId);

    List<Room> filterBy(RoomFilter filter);

}
