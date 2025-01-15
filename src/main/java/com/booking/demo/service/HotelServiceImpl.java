package com.booking.demo.service;

import com.booking.demo.exception.EntityNotFoundException;
import com.booking.demo.model.Hotel;
import com.booking.demo.repository.HotelRepository;
import com.booking.demo.utils.Strings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService{
    private final HotelRepository hotelRepository;
    @Override
    public Hotel save(Hotel hotel) {
        return hotelRepository.saveAndFlush(hotel);
    }

    @Override
    public Hotel update(Hotel updatingHotel) {
        Hotel hotel = findHotelById(updatingHotel.getId());
        hotel.setAddress(updatingHotel.getAddress());
        hotel.setName(updatingHotel.getName());
        hotel.setTitle(updatingHotel.getTitle());
        hotel.setTown(updatingHotel.getTown());
        hotel.setDistance(updatingHotel.getDistance());

        return hotelRepository.saveAndFlush(hotel);
    }

    @Override
    public Hotel findHotelById(String hotelId) {
        return hotelRepository.findById(hotelId)
                .orElseThrow(() -> {
                            throw new EntityNotFoundException(
                                    MessageFormat.format(Strings.ENTITY_NOT_EXISTS, "Отель", hotelId)
                            );
                        }
                );
    }

    @Override
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    @Override
    public void deleteHotelById(String hotelId) {
        hotelRepository.deleteById(hotelId);
    }
}
