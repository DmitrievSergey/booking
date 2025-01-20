package com.booking.demo.dto.roomdto.response;

import com.booking.demo.dto.response.ResponseHotelDto;
import com.booking.demo.model.Hotel;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseRoomDto {
    private String id;

    private String name;

    private String description;

    private String number;

    private Float pricePerDay;

    private Byte peopleCount;

    private ResponseHotelDto hotel;
}
