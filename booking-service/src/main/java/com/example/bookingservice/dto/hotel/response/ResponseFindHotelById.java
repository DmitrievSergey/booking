package com.example.bookingservice.dto.hotel.response;

import com.example.bookingservice.dto.roomdto.response.ResponseRoomWithoutHotel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseFindHotelById {
    private String id;

    private String name;

    private String title;

    private String town;

    private String address;

    private String distance;

    private Map<String, ResponseRoomWithoutHotel> roomMap;

}
