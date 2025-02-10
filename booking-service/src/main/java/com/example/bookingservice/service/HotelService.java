package com.example.bookingservice.service;


import com.example.bookingservice.dto.filter.HotelFilter;
import com.example.bookingservice.model.Hotel;

import java.util.List;

public interface HotelService {
    Hotel save(Hotel hotel);

    Hotel update(Hotel updatingHotel);

    Hotel findHotelById(String hotelId);

    List<Hotel> getAllHotels();

    void deleteHotelById(String hotelId);

    Hotel rateHotel(int rate, String hotelId);

    List<Hotel> filterBy(HotelFilter filter);

    List<Hotel> findAll();

}
