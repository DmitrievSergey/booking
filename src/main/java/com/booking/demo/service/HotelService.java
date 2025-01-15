package com.booking.demo.service;

import com.booking.demo.model.Hotel;

import java.util.List;

public interface HotelService {
    Hotel save(Hotel hotel);

    Hotel update(Hotel updatingHotel);

    Hotel findHotelById(String hotelId);

    List<Hotel> getAllHotels();

    void deleteHotelById(String hotelId);

}
