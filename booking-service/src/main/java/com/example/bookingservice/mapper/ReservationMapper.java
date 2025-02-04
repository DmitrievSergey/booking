package com.example.bookingservice.mapper;


import com.example.bookingservice.dto.reservation.request.RequestReserveRoom;
import com.example.bookingservice.dto.reservation.response.ResponseReservation;
import com.example.bookingservice.model.ReservationInterval;
import com.example.bookingservice.model.Room;
import com.example.bookingservice.model.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
    @Mapping(target = "room", source = "room")
    @Mapping(target = "id", ignore = true)
    ReservationInterval mapToEntity(Room room, RequestReserveRoom reserveRoom, User user);

    @Mapping(target = "user.name", source = "user.userName")
    @Mapping(target = "id", source = "id")
    ResponseReservation mapToResponse(ReservationInterval reservationInterval);


}
