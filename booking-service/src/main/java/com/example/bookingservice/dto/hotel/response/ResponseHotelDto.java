package com.example.bookingservice.dto.hotel.response;

import com.example.bookingservice.dto.hotel.request.HotelDto;
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
