package com.booking.demo.dto.hotel.response;

import com.booking.demo.dto.hotel.request.HotelDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseHotelDto extends HotelDto {
    private Float rating;
    private int numberOfRating;
}
