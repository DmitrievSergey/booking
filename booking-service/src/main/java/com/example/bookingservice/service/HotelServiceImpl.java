package com.example.bookingservice.service;


import com.example.bookingservice.dto.filter.HotelFilter;
import com.example.bookingservice.exception.EntityAlreadyExistsException;
import com.example.bookingservice.exception.EntityNotFoundException;
import com.example.bookingservice.model.Hotel;
import com.example.bookingservice.repository.HotelRepository;
import com.example.bookingservice.repository.HotelSpecification;
import com.example.bookingservice.utils.AppMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService{
    private final HotelRepository hotelRepository;
    @Override
    public synchronized Hotel save(Hotel hotel) {
        Optional<Hotel> findingHotel = hotelRepository.findByNameAndAddressAndTown(
                hotel.getName(),
                hotel.getAddress(),
                hotel.getTown()
        );
        if(findingHotel.isPresent()) throw new EntityAlreadyExistsException(
                MessageFormat.format(AppMessages.ENTITY_ALREADY_EXISTS, "Отель", hotel.getName())
        );
        return hotelRepository.saveAndFlush(hotel);
    }

    @Override
    public synchronized Hotel update(Hotel updatingHotel) {
        Optional<Hotel> findingHotel = hotelRepository.findByNameAndAddressAndTown(
                updatingHotel.getName(),
                updatingHotel.getAddress(),
                updatingHotel.getTown()
        );
        if(findingHotel.isPresent()
                && !updatingHotel.getId().equals(findingHotel.get().getId())) throw new EntityAlreadyExistsException(
                MessageFormat.format(AppMessages.ENTITY_ALREADY_EXISTS, "Отель", updatingHotel.getName())
        );
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
                                    MessageFormat.format(AppMessages.ENTITY_NOT_EXISTS, "Отель", hotelId)
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

    @Override
    public synchronized Hotel rateHotel(int rate, String hotelId) {
        Hotel hotel = findHotelById(hotelId);
        float rating = hotel.getRating();
        int numberOfRating = hotel.getNumberOfRating();
        int totalRating = (int) (numberOfRating * rating) + rate;
        numberOfRating += 1;
        hotel.setNumberOfRating(numberOfRating);
        hotel.setRating((float)totalRating/numberOfRating);

        return hotelRepository.saveAndFlush(hotel);
    }

    @Override
    public List<Hotel> filterBy(HotelFilter filter) {
        List<Hotel> hotelList = hotelRepository.findAll(HotelSpecification.withFilter(filter)
                , PageRequest.of(
                        filter.getPageNumber(),
                        filter.getPageSize()
                )).getContent();
        log.info("Hotel list {}", hotelList);
        return hotelList;
    }
}
