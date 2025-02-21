package com.example.bookingservice.controller;

import com.example.bookingservice.dto.hotel.request.HotelDto;
import com.example.bookingservice.mapper.HotelMapper;
import com.example.bookingservice.model.Hotel;
import com.example.bookingservice.service.HotelService;
import com.example.bookingservice.service.HotelServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.Mockito.when;

@WebMvcTest(HotelController.class)
@ExtendWith(MockitoExtension.class)
public class UnitTestsHotelController {
    @MockitoBean
    private HotelService hotelService;

    @MockitoBean
    private HotelMapper hotelMapper;

    @Test
    public void testCreateHotel() {
        when(hotelMapper.map(getHotelDto())).thenReturn(getHotel());
    }

    private HotelDto getHotelDto() {
        HotelDto hotelDto = new HotelDto();
        hotelDto.setName("First Hotel");
        hotelDto.setAddress("Tverskaja street 1");
        hotelDto.setTitle("Title of hotel");
        hotelDto.setTown("Moscow");
        hotelDto.setDistance("9.0");

        return hotelDto;
    }

    private Hotel getHotel() {
        return new Hotel();
    }
}
