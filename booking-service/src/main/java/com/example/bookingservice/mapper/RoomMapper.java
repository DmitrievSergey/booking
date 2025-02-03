package com.example.bookingservice.mapper;

import com.example.bookingservice.dto.roomdto.request.CreateRoomDto;
import com.example.bookingservice.dto.roomdto.response.ResponseRoomDto;
import com.example.bookingservice.model.Room;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    Room map(CreateRoomDto roomDto);

    ResponseRoomDto map(Room room);
}
