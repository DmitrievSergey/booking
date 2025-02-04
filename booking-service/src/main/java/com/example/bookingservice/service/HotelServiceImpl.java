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
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {
    ReentrantLock lock = new ReentrantLock();
    private final HotelRepository hotelRepository;

    @Override
    public Hotel save(Hotel hotel) {

        return checkAndSave(hotel);
    }

    @Override
    public Hotel update(Hotel updatingHotel) {
        lock.lock();
        Optional<Hotel> findingHotel = hotelRepository.findByNameAndAddressAndTown(
                updatingHotel.getName(),
                updatingHotel.getAddress(),
                updatingHotel.getTown()
        );
        if (findingHotel.isPresent()
                && !updatingHotel.getId().equals(findingHotel.get().getId())) {
            lock.unlock();
            throw new EntityAlreadyExistsException(
                    MessageFormat.format(AppMessages.ENTITY_ALREADY_EXISTS, "Отель", updatingHotel.getName())
            );
        }
        Hotel hotel = findHotelById(updatingHotel.getId());
        hotel.setAddress(updatingHotel.getAddress());
        hotel.setName(updatingHotel.getName());
        hotel.setTitle(updatingHotel.getTitle());
        hotel.setTown(updatingHotel.getTown());
        hotel.setDistance(updatingHotel.getDistance());
        hotelRepository.saveAndFlush(hotel);
        lock.unlock();
        return hotel;
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
    public Hotel rateHotel(int rate, String hotelId) {
        lock.lock();
        Hotel hotel = findHotelById(hotelId);
        float rating = hotel.getRating();
        int numberOfRating = hotel.getNumberOfRating();
        int totalRating = (int) (numberOfRating * rating) + rate;
        numberOfRating += 1;
        hotel.setNumberOfRating(numberOfRating);
        hotel.setRating((float) totalRating / numberOfRating);
        hotelRepository.saveAndFlush(hotel);
        lock.unlock();
        return findHotelById(hotelId);
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

    private Hotel checkAndSave(Hotel hotel) {
        lock.lock();
        if (hotelRepository.findByNameAndAddressAndTown(
                hotel.getName(),
                hotel.getAddress(),
                hotel.getTown()).orElse(null) != null) {
            lock.unlock();
            throw new EntityAlreadyExistsException(
                    MessageFormat.format(AppMessages.ENTITY_ALREADY_EXISTS, "Отель", hotel.getName())
            );
        }

        Hotel savedHotel = hotelRepository.saveAndFlush(hotel);
        lock.unlock();
        return savedHotel;
    }
}
