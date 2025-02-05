package com.example.bookingservice.controller;

import com.example.bookingservice.AbstractTest;
import com.example.bookingservice.dto.hotel.request.HotelDto;
import com.example.bookingservice.dto.roomdto.request.CreateRoomDto;
import com.example.bookingservice.mapper.HotelMapper;
import com.example.bookingservice.mapper.RoomMapper;
import com.example.bookingservice.model.Hotel;
import com.example.bookingservice.model.RoleType;
import com.example.bookingservice.model.Room;
import com.example.bookingservice.model.User;
import com.example.bookingservice.service.HotelService;
import com.example.bookingservice.service.RoomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestRoomController extends AbstractTest {
    @Autowired
    private HotelService hotelService;


    @Test
    @WithMockUser(username = "Alex", roles = "ADMIN")
    public void whenAdminCreateRoom_ThenRoomCreated() throws Exception {
        Hotel hotel = new Hotel();
        hotel.setName("First hotel");
        hotel.setTitle("First hotel title");
        hotel.setAddress("Tverskaja Street 1");
        hotel.setTown("Moscow");
        hotel = hotelService.save(hotel);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/room/add")
                        .content(asJsonString(new CreateRoomDto(
                                "king room"
                                , "King room with sea view"
                                , "1"
                                , 500.0f
                                ,(byte)2
                                , hotel)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("king room"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("King room with sea view"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.number").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pricePerDay").value(500.0f))
                .andExpect(MockMvcResultMatchers.jsonPath("$.peopleCount").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.hotel").value(hotel))
                .andExpect(MockMvcResultMatchers.jsonPath("$.hotel.id").value(hotel.getId()));
    }
}
