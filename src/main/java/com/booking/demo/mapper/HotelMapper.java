package com.booking.demo.mapper;

import com.booking.demo.dto.request.HotelDto;
import com.booking.demo.dto.response.ResponseHotelDto;
import com.booking.demo.model.Hotel;
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
}
