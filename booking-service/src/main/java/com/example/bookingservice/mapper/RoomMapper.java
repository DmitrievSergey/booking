package com.example.bookingservice.mapper;

import com.example.bookingservice.dto.roomdto.request.CreateRoomDto;
import com.example.bookingservice.dto.roomdto.response.ResponseRoomDto;
import com.example.bookingservice.model.Hotel;
import com.example.bookingservice.model.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    Room map(CreateRoomDto roomDto);

    @Mapping(target = "hotel", source = "hotel")
    @Mapping(target = "name", source="roomDto.name")
    @Mapping(target = "id", ignore = true)
    Room map(CreateRoomDto roomDto, Hotel hotel);

    ResponseRoomDto map(Room room);
}
