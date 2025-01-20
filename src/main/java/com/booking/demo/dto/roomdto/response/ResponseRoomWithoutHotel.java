package com.booking.demo.dto.roomdto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseRoomWithoutHotel {
    private String id;

    private String name;

    private String description;

    private String number;

    private Float pricePerDay;

    private Byte peopleCount;
}
