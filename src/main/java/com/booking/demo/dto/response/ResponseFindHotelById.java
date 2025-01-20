package com.booking.demo.dto.response;

import com.booking.demo.dto.roomdto.response.ResponseRoomDto;
import com.booking.demo.dto.roomdto.response.ResponseRoomWithoutHotel;
import com.booking.demo.model.Room;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
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

    private Map<String,ResponseRoomWithoutHotel> roomMap;

}
