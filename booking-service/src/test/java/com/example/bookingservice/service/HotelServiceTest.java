package com.example.bookingservice.service;


import com.example.bookingservice.exception.EntityAlreadyExistsException;
import com.example.bookingservice.model.Hotel;
import com.example.bookingservice.repository.HotelRepository;
import com.example.bookingservice.utils.AppMessages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.text.MessageFormat;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@WebMvcTest(HotelServiceImpl.class)
@ExtendWith(MockitoExtension.class)
public class HotelServiceTest {

    private Hotel hotel;
    private Hotel savedHotel;
    private Hotel returnedHotel;
    private Optional<Hotel> optionalHotel;
    private Hotel updatingHotel;
    private Hotel findingHotel;

    private String returnedHotelId = UUID.randomUUID().toString();

//    @Autowired
//    private HotelService hotelService;

    @Autowired
    private HotelServiceImpl hotelServiceImpl;

    @MockitoBean
    private HotelRepository hotelRepository;


    @BeforeEach
    public void setUp() {
        hotel = new Hotel(
                "hotel name"
                , "hotel title"
                , "Moscow"
                , "address 1"
                , "9"
                , 0f
                , 0);

        savedHotel = new Hotel(
                UUID.randomUUID().toString()
                , "hotel name"
                , "hotel title"
                , "Moscow"
                , "address 1"
                , "9"
                , 0f
                , 0);

        returnedHotel = new Hotel(
                returnedHotelId
                , "hotel name"
                , "hotel title"
                , "Moscow"
                , "address 1"
                , "9"
                , 0f
                , 0);

        optionalHotel = Optional.of(new Hotel(
                UUID.randomUUID().toString()
                , "hotel name"
                , "hotel title"
                , "Moscow"
                , "address 1"
                , "9"
                , 0f
                , 0));

        updatingHotel = new Hotel(
                returnedHotelId
                , "hotel name updated"
                , "hotel title updated"
                , "Moscow updated"
                , "address 1 updated"
                , "10"
                , 0f
                , 0);

        findingHotel = new Hotel(
                returnedHotelId
                , "hotel name updated"
                , "hotel title updated"
                , "Moscow updated"
                , "address 1 updated"
                , "10"
                , 0f
                , 0);
    }

    @Test
    public void whenSaveHotel_ThanHotelSaved() {
        when(hotelRepository.findByNameAndAddressAndTown(
                hotel.getName()
                , hotel.getAddress()
                , hotel.getTown())).thenReturn(Optional.empty());
        when(hotelRepository.saveAndFlush(hotel)).thenReturn(savedHotel);
        assertDoesNotThrow(() -> {
            Hotel actualHotel = hotelServiceImpl.save(hotel);
            assertEquals(actualHotel, savedHotel);
        });

        Mockito.verify(hotelRepository, times(1)).findByNameAndAddressAndTown(hotel.getName()
                , hotel.getAddress()
                , hotel.getTown());
        Mockito.verify(hotelRepository, times(1)).saveAndFlush(hotel);

    }

    @Test
    public void whenSaveExistedHotel_ThanException() {
        when(hotelRepository.findByNameAndAddressAndTown(hotel.getName()
                , hotel.getAddress()
                , hotel.getTown())).thenReturn(Optional.of(hotel));
        when(hotelRepository.saveAndFlush(hotel)).thenReturn(savedHotel);

        assertThrowsExactly(EntityAlreadyExistsException.class
                , () -> {
                    Hotel actualHotel = hotelServiceImpl.save(hotel);

                    throw new EntityAlreadyExistsException(
                            MessageFormat.format(AppMessages.ENTITY_ALREADY_EXISTS, "Отель", hotel.getName())
                    );
                });

        Mockito.verify(hotelRepository, times(1)).findByNameAndAddressAndTown(hotel.getName()
                , hotel.getAddress()
                , hotel.getTown());
        Mockito.verify(hotelRepository, times(0)).saveAndFlush(hotel);
    }

    @Test
    public void whenUpdateExistedHotel_ThanHotelUpdated() {
        when(hotelRepository.findByNameAndAddressAndTown(updatingHotel.getName()
                , updatingHotel.getAddress()
                , updatingHotel.getTown())).thenReturn(Optional.of(returnedHotel));
        when(hotelRepository.findById(updatingHotel.getId())).thenReturn(Optional.ofNullable(returnedHotel));

        when(hotelRepository.saveAndFlush(returnedHotel)).thenReturn(updatingHotel);



        Hotel actualHotel = hotelServiceImpl.update(updatingHotel);


        Mockito.verify(hotelRepository, times(1)).findByNameAndAddressAndTown(
                updatingHotel.getName()
                , updatingHotel.getAddress()
                , updatingHotel.getTown());
        Mockito.verify(hotelRepository, times(1)).saveAndFlush(returnedHotel);
    }
}
