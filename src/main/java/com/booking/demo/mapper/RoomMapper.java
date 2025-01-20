package com.booking.demo.mapper;

import com.booking.demo.dto.roomdto.request.CreateRoomDto;
import com.booking.demo.dto.roomdto.response.ResponseRoomDto;
import com.booking.demo.model.Room;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    Room map(CreateRoomDto roomDto);

    ResponseRoomDto map(Room room);
}
