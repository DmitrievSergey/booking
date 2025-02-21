package com.example.bookingservice.controller;

import com.example.bookingservice.AbstractTest;

import com.example.bookingservice.KafkaBaseTest;
import com.example.bookingservice.dto.reservation.request.RequestReserveRoom;

import com.example.bookingservice.exception.EntityAlreadyExistsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;

import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.time.LocalDate;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Sql(scripts = "classpath:db/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class TestReservationController extends AbstractTest implements KafkaBaseTest {

    private String hotelId = "e913b22d-5d21-4998-ae8f-a258fca8913f";
    private String anotherHotelId = "e913b22d-5d21-4998-ae8f-a258fca8913d";
    private String roomId = "73afa3b2-067a-4b68-acc1-3cfed494fe8a";
    private String roomIdAnotherHotel = "73afa3b2-067a-4b68-acc1-3cfed494fe8b";
    private String adminId = "06b9db4c-f0c3-4737-9d10-22e6cf3fc6ba";
    private String userId = "10badb70-7001-4511-8d5f-188a927a212f";
    private String nonExistingRoomId = "4c5fb29a-eb4d-4485-a1f5-801de75f9335";


    @ParameterizedTest()
    @CsvSource({"1977-04-01, 1977-04-05"})
    @DisplayName("Test create reservation by admin")
    @Sql("classpath:db/createRoom.sql")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl"
            , value = "Alex"
            , setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void whenAdminCreateRoomReservation_ThenReservationCreated(LocalDate dateStart, LocalDate dateEnd) throws Exception {


        String userId = userService.findByUserName("Alex").getId();
        post("/api/v1/reservation/{roomId}", roomId, new RequestReserveRoom(
                dateStart
                , dateEnd
        )).andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate").value(dateStart.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endDate").value(dateEnd.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.room.id").value(roomId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.room.hotel.id").value(hotelId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.id").value(userId));
    }

    @ParameterizedTest()
    @CsvSource({"1977-02-10, 1977-02-28"
    , "1977-03-06, 1977-03-10"})
    @DisplayName("Test create reservation with non intersection interval for room with existing reservation" +
            " by admin")
    @Sql("classpath:db/createRoom.sql")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl"
            , value = "Alex"
            , setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void whenAdminCreateRoomReservationWithNonIntersectionInterval_ThenReservationCreated(LocalDate dateStart, LocalDate dateEnd) throws Exception {
        String userId = userService.findByUserName("Alex").getId();
        post("/api/v1/reservation/{roomId}", roomId, new RequestReserveRoom(
                dateStart
                , dateEnd
        )).andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate").value(dateStart.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endDate").value(dateEnd.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.room.id").value(roomId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.room.hotel.id").value(hotelId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.id").value(userId));
    }

    @ParameterizedTest()
    @CsvSource({"1977-03-01, 1977-03-05"})
    @DisplayName("Test create reservations with same interval for different hotel and one room number" +
            " by admin")
    @Sql("classpath:db/createReservationForTwoHotels.sql")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl"
            , value = "Alex"
            , setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void whenAdminCreateRoomReservationForDifferentHotel_ThenReservationCreated(LocalDate dateStart, LocalDate dateEnd) throws Exception {
        String userId = userService.findByUserName("Alex").getId();
        post("/api/v1/reservation/{roomId}", roomIdAnotherHotel, new RequestReserveRoom(
                dateStart
                , dateEnd
        )).andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate").value(dateStart.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endDate").value(dateEnd.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.room.id").value(roomIdAnotherHotel))
                .andExpect(MockMvcResultMatchers.jsonPath("$.room.hotel.id").value(anotherHotelId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.id").value(userId));
    }





    @DisplayName("Test try to create interval with intersect with existing interval  by admin")
    @Sql(value = "classpath:db/createReservation.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @ParameterizedTest()
    @CsvSource({"1977-03-01, 1977-03-05"
    , "1977-02-28, 1977-03-03"
    , "1977-03-02, 1977-03-06"
    , "1977-03-02, 1977-03-04"
    , "1977-02-28, 1977-03-07"})
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl"
            , value = "Alex"
            , setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void whenAdminCreateRoomReservationWithExistsDate_ThenException(LocalDate startDate, LocalDate endDate) throws Exception {
        post("/api/v1/reservation/{roomId}", roomId, new RequestReserveRoom(
                startDate
                , endDate
        ))
                .andExpect(status().isConflict())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityAlreadyExistsException))
                .andExpect(result -> assertEquals("Интервал with name "+ startDate +" - "+ endDate +" already exists", result.getResolvedException().getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Интервал with name "+ startDate +" - "+ endDate +" already exists"));;

    }


}
