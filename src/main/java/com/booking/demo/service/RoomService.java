package com.booking.demo.service;

import com.booking.demo.model.Room;

public interface RoomService {
    Room save(Room room);

    Room update(String roomId, Room room);

    Room findRoomById(String roomId);

    void deleteRoomById(String roomId);

}
