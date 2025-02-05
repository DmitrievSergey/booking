package com.example.bookingservice.controller;

import com.example.bookingservice.AbstractTest;
import com.example.bookingservice.controller.HotelController;
import com.example.bookingservice.dto.hotel.request.HotelDto;
import com.example.bookingservice.model.Hotel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestHotelRestController extends AbstractTest {


    @Test
    @WithMockUser(username = "Alex", roles = "ADMIN")
    public void whenAdminCreateHotel_ThenHotelCreated() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/hotel/add")
                        .content(asJsonString(new HotelDto(
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Title"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.town").value("Moscow"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("address 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.distance").value("9"));
    }

    @Test
    @WithMockUser(username = "Roy", roles = "USER")
    public void whenUserCreateHotel_ThenHotelDoesNotCreated() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/hotel/add")
                        .content(asJsonString(new HotelDto(
                                "firstHotel"
                                , "Title with 10 simbols"
                                , "Moscow"
                                , "address 1"
                                , "9")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Access Denied"));
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl"
            , value = "Alex"
            , setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void whenHotelWithExistingNameAddressTownCreated_ThenException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/hotel/add")
                        .content(asJsonString(new HotelDto(
                                "firstHotel"
                                , "Title with 10 simbols"
                                , "Moscow"
                                , "address 1"
                                , "9")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Access Denied"));
    }

}
