package com.booking.demo.dto.response;

import com.booking.demo.dto.request.HotelDto;
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
    private Long gradeCount;
}
