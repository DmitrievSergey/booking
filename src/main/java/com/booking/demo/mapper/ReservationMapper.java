package com.booking.demo.mapper;

import com.booking.demo.dto.reservation.request.RequestReserveRoom;
import com.booking.demo.dto.reservation.response.ResponseReservation;
import com.booking.demo.model.ReservationInterval;
import com.booking.demo.security.AppUserDetails;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
    ReservationInterval mapToEntity(String roomId, RequestReserveRoom reserveRoom, AppUserDetails user);

    ResponseReservation mapToResponse(ReservationInterval reservationInterval);
}
