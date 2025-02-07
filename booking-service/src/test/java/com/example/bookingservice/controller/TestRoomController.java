package com.example.bookingservice.controller;

import com.example.bookingservice.AbstractTest;
import com.example.bookingservice.PostgreBaseTest;
import com.example.bookingservice.dto.roomdto.request.CreateRoomDto;
import com.example.bookingservice.exception.EntityAlreadyExistsException;
import com.example.bookingservice.exception.EntityNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class TestRoomController extends AbstractTest {

    private String hotelId = "e913b22d-5d21-4998-ae8f-a258fca8913f";
    private String roomId = "73afa3b2-067a-4b68-acc1-3cfed494fe8a";
    private String nonExistingRoomId = "4c5fb29a-eb4d-4485-a1f5-801de75f9335";


    @Test
    @DisplayName("Test room create by admin")
    @WithMockUser(username = "Alex", roles = "ADMIN")
    @Sql("classpath:db/createHotel.sql")
    public void whenAdminCreateRoom_ThenRoomCreated() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/room/add").param("hotelId",hotelId)
                        .content(objectMapper.writeValueAsString(new CreateRoomDto(
                                "king room"
                                , "King room with sea view"
                                , "1"
                                , 500.0f
                                ,(byte)2)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("king room"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("King room with sea view"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.number").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pricePerDay").value(500.0f))
                .andExpect(MockMvcResultMatchers.jsonPath("$.peopleCount").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.hotel.id").value(hotelId));
    }

    @Test
    @DisplayName("Test create room with existing number in hotel create by admin")
    @WithMockUser(username = "Alex", roles = "ADMIN")
    @Sql("classpath:db/createRoom.sql")
    public void whenAdminCreateRoomWithExistingNumberInHotel_Then500() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/room/add").param("hotelId",hotelId)
                        .content(objectMapper.writeValueAsString(new CreateRoomDto(
                                "king room"
                                , "King room with sea view"
                                , "1"
                                , 500.0f
                                ,(byte)2)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityAlreadyExistsException))
                .andExpect(result -> assertEquals("Комната with name 1 already exists", result.getResolvedException().getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Комната with name 1 already exists"));
    }

    @Test
    @DisplayName("Test create room with non existing hotel create by admin")
    @WithMockUser(username = "Alex", roles = "ADMIN")
    @Sql("classpath:db/createHotel.sql")
    public void whenAdminCreateRoomWithNonExistingHotel_Then404() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/room/add").param("hotelId","123")
                        .content(objectMapper.writeValueAsString(new CreateRoomDto(
                                "king room"
                                , "King room with sea view"
                                , "1"
                                , 500.0f
                                ,(byte)2)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> assertEquals("Отель with ID 123 not exists", result.getResolvedException().getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Отель with ID 123 not exists"));
    }

    @Test
    @DisplayName("Test room change by admin")
    @WithMockUser(username = "Alex", roles = "ADMIN")
    @Sql("classpath:db/createRoom.sql")
    public void whenAdminChangeRoom_ThenRoomChanged() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/room/{roomId}", roomId)
                        .content(objectMapper.writeValueAsString(new CreateRoomDto(
                                "king room updated"
                                , "King room with sea view updated"
                                , "10"
                                , 1000.0f
                                ,(byte)3)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("king room updated"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("King room with sea view updated"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.number").value("10"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pricePerDay").value(1000.0f))
                .andExpect(MockMvcResultMatchers.jsonPath("$.peopleCount").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.hotel.id").value(hotelId));
    }

    @Test
    @DisplayName("Test room create by user")
    @WithMockUser(username = "Alex", roles = "USER")
    @Sql("classpath:db/createRoom.sql")
    public void whenUserCreateRoom_Then401() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/room/add").param("hotelId", hotelId)
                        .content(objectMapper.writeValueAsString(new CreateRoomDto(
                                "king room updated"
                                , "King room with sea view updated"
                                , "1"
                                , 1000.0f
                                ,(byte)3)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Access Denied"));
    }

    @Test
    @DisplayName("Test room change by user")
    @WithMockUser(username = "Alex", roles = "USER")
    @Sql("classpath:db/createRoom.sql")
    public void whenUserChangeRoom_Then401() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/room/{roomId}", roomId)
                        .content(objectMapper.writeValueAsString(new CreateRoomDto(
                                "king room updated"
                                , "King room with sea view updated"
                                , "1"
                                , 1000.0f
                                ,(byte)3)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Access Denied"));
    }

    @Test
    @DisplayName("Test delete non existing room by admin")
    @WithMockUser(username = "Alex", roles = "ADMIN")
    @Sql("classpath:db/createRoom.sql")
    public void whenAdminDeleteNonExistingRoom_Then404() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/room/{roomId}", nonExistingRoomId))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> assertEquals("Комната with ID " + nonExistingRoomId + " not exists", result.getResolvedException().getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Комната with ID " + nonExistingRoomId + " not exists"));
    }

    @Test
    @DisplayName("Test delete existing room by admin")
    @WithMockUser(username = "Alex", roles = "ADMIN")
    @Sql("classpath:db/createRoom.sql")
    public void whenAdminDeleteExistingRoom_Then200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/room/{roomId}", roomId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Комната с Id " + roomId + " удалена"));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
