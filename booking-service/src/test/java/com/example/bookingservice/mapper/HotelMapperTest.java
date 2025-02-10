package com.example.bookingservice.mapper;

import com.example.bookingservice.dto.hotel.request.HotelDto;
import com.example.bookingservice.dto.hotel.response.ResponseFindHotelById;
import com.example.bookingservice.dto.hotel.response.ResponseHotelDto;
import com.example.bookingservice.dto.roomdto.response.ResponseRoomWithoutHotel;
import com.example.bookingservice.model.Hotel;
import com.example.bookingservice.model.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(HotelMapper.class)
@ExtendWith(MockitoExtension.class)
public class HotelMapperTest {

    private Room room;
    private HotelDto hotelDto;
    private Hotel hotel;
    private Hotel hotelWithRoom;
    private String hotelId;

    @Autowired
    private HotelMapper hotelMapper;

    @BeforeEach
    public void setUp() {
        hotelWithRoom = new Hotel(
                UUID.randomUUID().toString()
                ,"hotel name"
                , "hotel title"
                , "Moscow"
                , "address 1"
                , "9"
                ,0f
                ,0);
        room = new Room (
                UUID.randomUUID().toString()
                ,"first room"
                ,"first room description"
                ,"1"
                ,500f
                ,(byte)2
                ,hotelWithRoom
        );
        hotelWithRoom.addRoom(room);
        hotel = new Hotel(
                UUID.randomUUID().toString()
                ,"hotel name"
                , "hotel title"
                , "Moscow"
                , "address 1"
                , "9"
        ,0f
        ,0);
        hotelDto = new HotelDto(
                "hotel name"
                , "hotel title"
                , "Moscow"
                , "address 1"
                , "9"
        );

        hotelId = UUID.randomUUID().toString();
    }

    @Test
    public void hotelDtoConvertToHotel_thanConverted() {
        Hotel hotel = hotelMapper.map(hotelDto);
        assertEquals(hotelDto.getName(),hotel.getName());
        assertEquals(hotelDto.getTitle(),hotel.getTitle());
        assertEquals(hotelDto.getAddress(), hotel.getAddress());
        assertEquals(hotelDto.getTown(),hotel.getTown());
        assertEquals(hotelDto.getDistance(), hotel.getDistance());
        assertTrue(hotel.getRoomMap().isEmpty());
        assertNull(hotel.getId());
    }

    @Test
    public void hotelConvertToResponseHotelDto_thenConverted() {
        ResponseHotelDto responseHotelDto = hotelMapper.mapToResponse(hotelWithRoom);

        assertEquals(hotelWithRoom.getId(),responseHotelDto.getId());
        assertEquals(hotelWithRoom.getName(),responseHotelDto.getName());
        assertEquals(hotelWithRoom.getTitle(),responseHotelDto.getTitle());
        assertEquals(hotelWithRoom.getAddress(), responseHotelDto.getAddress());
        assertEquals(hotelWithRoom.getTown(),responseHotelDto.getTown());
        assertEquals(hotelWithRoom.getDistance(), responseHotelDto.getDistance());
        assertInstanceOf(ResponseHotelDto.class, responseHotelDto);
    }

    @Test
    public void hotelConvertToResponseFindHotelById_thenConverted() {
        ResponseFindHotelById hotelById = hotelMapper.mapToResponseWithRooms(hotelWithRoom);

        assertEquals(hotelWithRoom.getId(),hotelById.getId());
        assertEquals(hotelWithRoom.getName(),hotelById.getName());
        assertEquals(hotelWithRoom.getTitle(),hotelById.getTitle());
        assertEquals(hotelWithRoom.getAddress(), hotelById.getAddress());
        assertEquals(hotelWithRoom.getTown(),hotelById.getTown());
        assertEquals(hotelWithRoom.getDistance(), hotelById.getDistance());
        assertTrue(hotelById.getRoomMap().containsKey("1"));
        assertEquals(hotelWithRoom.getRoomMap().get("1").getId()
        ,hotelById.getRoomMap().get("1").getId());
        assertEquals(hotelWithRoom.getRoomMap().get("1").getName()
                ,hotelById.getRoomMap().get("1").getName());
        assertInstanceOf(ResponseRoomWithoutHotel.class
                , hotelById.getRoomMap().get("1"));

    }

    @Test
    public void hotelDtoAndIdConvertToHotel_thanConverted() {
        Hotel hotel = hotelMapper.map(hotelId, hotelDto);
        assertEquals(hotel.getId(), hotelId);
        assertEquals(hotelDto.getName(),hotel.getName());
        assertEquals(hotelDto.getTitle(),hotel.getTitle());
        assertEquals(hotelDto.getAddress(), hotel.getAddress());
        assertEquals(hotelDto.getTown(),hotel.getTown());
        assertEquals(hotelDto.getDistance(), hotel.getDistance());
        assertTrue(hotel.getRoomMap().isEmpty());
    }

}
