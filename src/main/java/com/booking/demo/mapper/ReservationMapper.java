package com.booking.demo.mapper;

import com.booking.demo.dto.reservation.request.RequestReserveRoom;
import com.booking.demo.dto.reservation.response.ResponseReservation;
import com.booking.demo.model.ReservationInterval;
import com.booking.demo.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
    @Mapping( target = "id", source = "roomId" )
    ReservationInterval mapToEntity(String roomId, RequestReserveRoom reserveRoom, User user);

    ResponseReservation mapToResponse(ReservationInterval reservationInterval);


}
