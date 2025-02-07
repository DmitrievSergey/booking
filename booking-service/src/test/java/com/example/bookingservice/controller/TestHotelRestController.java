package com.example.bookingservice.controller;

import com.example.bookingservice.AbstractTest;
import com.example.bookingservice.PostgreBaseTest;
import com.example.bookingservice.dto.hotel.request.HotelDto;
import com.example.bookingservice.exception.EntityAlreadyExistsException;
import com.example.bookingservice.exception.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestHotelRestController extends AbstractTest {
    private String hotelId = "e913b22d-5d21-4998-ae8f-a258fca8913f";
    private String nonExistingHotelId = "a913b22d-5d21-4998-ae8f-a258fca8913f";

    private String existingName = "First hotel";
    private String existingAddress = "Tverskaja Street 1";
    private String existingTown = "Moscow";
    private String updatedName = "First hotel updated";
    private String updatedAddress = "Tverskaja Street 1 updated";
    private String updatedTitle = "First hotel title updated";
    private String updatedTown = "Moscow updated";

    @Test
    @DisplayName("Create hotel by admin")
    @WithMockUser(username = "Alex", roles = "ADMIN")
    public void whenAdminCreateHotel_ThenHotelCreated() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/hotel/add")
                        .content(objectMapper.writeValueAsString(new HotelDto(
                                "firstHotel"
                                , "Title with 10 simbols"
                                , "Moscow"
                                , "address 1"
                                , "9")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("firstHotel"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Title with 10 simbols"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.town").value("Moscow"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("address 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.distance").value("9"));
    }

    @Test
    @DisplayName("Update existing hotel by admin")
    @Sql("classpath:db/createHotel.sql")
    @WithMockUser(username = "Alex", roles = "ADMIN")
    public void whenAdminUpdateHotel_ThenHotelUpdated() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/hotel/{id}", hotelId)
                        .content(objectMapper.writeValueAsString(new HotelDto(
                                updatedName
                                , updatedTitle
                                , updatedTown
                                , updatedAddress
                                , "9")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(hotelId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(updatedName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(updatedTitle))
                .andExpect(MockMvcResultMatchers.jsonPath("$.town").value(updatedTown))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value(updatedAddress))
                .andExpect(MockMvcResultMatchers.jsonPath("$.distance").value("9"));
    }

    @Test
    @DisplayName("Update non existing hotel by admin")
    @WithMockUser(username = "Alex", roles = "ADMIN")
    public void whenAdminUpdateNonExistingHotel_Then404() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/hotel/{id}", hotelId)
                        .content(objectMapper.writeValueAsString(new HotelDto(
                                updatedName
                                , updatedTitle
                                , updatedTown
                                , updatedAddress
                                , "9")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> assertEquals("Отель with ID "+ hotelId +" not exists", result.getResolvedException().getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Отель with ID "+ hotelId +" not exists"));
    }

    @Test
    @DisplayName("Create hotel by user")
    @WithMockUser(username = "Roy", roles = "USER")
    public void whenUserCreateHotel_ThenHotelDoesNotCreated() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/hotel/add")
                        .content(objectMapper.writeValueAsString(new HotelDto(
                                "First hotel"
                                , "First hotel title"
                                , "Moscow"
                                , "Tverskaja Street 1"
                                , "9")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Access Denied"));
    }

    @Test
    @DisplayName("Create hotel by admin with existing name, address and town")
    @Sql("classpath:db/createHotel.sql")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl"
            , value = "Alex"
            , setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void whenHotelWithExistingNameAddressTownCreated_ThenException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/hotel/add")
                        .content(objectMapper.writeValueAsString(new HotelDto(
                                existingName
                                , "First hotel title"
                                , existingTown
                                , existingAddress
                                , "9")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityAlreadyExistsException))
                .andExpect(result -> assertEquals("Отель with name "+ existingName +" already exists", result.getResolvedException().getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Отель with name "+ existingName +" already exists"));
    }

    @Test
    @DisplayName("Delete hotel with rooms")
    @Sql("classpath:db/createRoom.sql")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl"
            , value = "Alex"
            , setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void whenDeleteHotelWithRooms_ThenOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/hotel/{id}", hotelId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Отель с Id "+ hotelId +" удален"));
    }

    @Test
    @DisplayName("Delete hotel without rooms")
    @Sql("classpath:db/createHotel.sql")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl"
            , value = "Alex"
            , setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void whenDeleteHotelWithOutRooms_ThenOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/hotel/{id}", hotelId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Отель с Id "+ hotelId +" удален"));
    }

    @Test
    @DisplayName("Delete non existing hotel")
    @Sql("classpath:db/createHotel.sql")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl"
            , value = "Alex"
            , setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void whenDeleteNonExistingHotel_ThenException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/hotel/{id}", nonExistingHotelId))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> assertEquals("Отель with ID "+ nonExistingHotelId +" not exists", result.getResolvedException().getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Отель with ID "+ nonExistingHotelId +" not exists"));
    }

}
