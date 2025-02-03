package com.example.bookingservice.mapper;

import com.example.bookingservice.dto.hotel.request.HotelDto;
import com.example.bookingservice.dto.hotel.response.ResponseFindHotelById;
import com.example.bookingservice.dto.hotel.response.ResponseHotelDto;
import com.example.bookingservice.model.Hotel;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HotelMapper {
    HotelDto map(Hotel hotel);
    @InheritInverseConfiguration
    Hotel map(HotelDto hotelDto);

    @Mapping( target = "id", source = "hotelId" )
    Hotel map(String hotelId, HotelDto hotelDto);

    ResponseHotelDto mapToResponse(Hotel hotel);

    ResponseFindHotelById mapToResponseWithRooms(Hotel hotel);
}
