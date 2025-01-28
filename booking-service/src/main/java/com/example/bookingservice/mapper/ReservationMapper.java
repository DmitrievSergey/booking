package com.example.bookingservice.mapper;


import com.example.bookingservice.dto.reservation.request.RequestReserveRoom;
import com.example.bookingservice.dto.reservation.response.ResponseReservation;
import com.example.bookingservice.model.ReservationInterval;
import com.example.bookingservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
    @Mapping( target = "id", source = "roomId" )
    ReservationInterval mapToEntity(String roomId, RequestReserveRoom reserveRoom, User user);

    ResponseReservation mapToResponse(ReservationInterval reservationInterval);


}
