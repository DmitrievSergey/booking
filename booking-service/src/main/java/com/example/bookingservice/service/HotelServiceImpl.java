package com.example.bookingservice.service;


import com.example.bookingservice.dto.filter.HotelFilter;
import com.example.bookingservice.exception.EntityAlreadyExistsException;
import com.example.bookingservice.exception.EntityNotFoundException;
import com.example.bookingservice.model.Hotel;
import com.example.bookingservice.repository.HotelRepository;
import com.example.bookingservice.repository.HotelSpecification;
import com.example.bookingservice.utils.AppMessages;
import com.example.bookingservice.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {
    ReentrantLock lock = new ReentrantLock();
    private final HotelRepository hotelRepository;

    @Override
    @Caching(cacheable = {
            @Cacheable(value = "HotelService::findHotelByNameAndAddressAndTown", key = "#hotel.name + '.' + #hotel.address + '.' + #hotel.town")
    })
    public Hotel save(Hotel hotel) {
        log.info("Зашли в save");
        return checkAndSaveTransactional(hotel);
    }

    @Override
    @Caching(put = {
            @CachePut(value = "UserService::findHotelById", key = "#updatingHotel.id"),
            @CachePut(value = "HotelService::findHotelByNameAndAddressAndTown", key = "#updatingHotel.name + #updatingHotel.address + #updatingHotel.town")
    })
    public Hotel update(Hotel updatingHotel) {
        lock.lock();
        Optional<Hotel> findingHotel = findHotelByNameAndAddressAndTown(updatingHotel);
        if (findingHotel.isPresent()
                && !updatingHotel.getId().equals(findingHotel.get().getId())) {
            lock.unlock();
            throw new EntityAlreadyExistsException(
                    MessageFormat.format(AppMessages.ENTITY_ALREADY_EXISTS, "Отель", updatingHotel.getName())
            );
        }


        Hotel hotel = findHotelById(updatingHotel.getId());
        BeanUtils.copyNonNullProperties(updatingHotel, hotel);
        hotelRepository.saveAndFlush(hotel);
        lock.unlock();
        return hotel;


    }

    @Override
    @Cacheable(value = "UserService::findHotelById", key = "#hotelId")
    public Hotel findHotelById(String hotelId) {
        return hotelRepository.findById(hotelId)
                .orElseThrow(() -> new EntityNotFoundException(

                        MessageFormat.format(AppMessages.ENTITY_NOT_EXISTS, "Отель", hotelId)

                ));
    }

    @Override
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "UserService::findHotelById", key = "#hotel.id"),
            @CacheEvict(value = "HotelService::findHotelByNameAndAddressAndTown", key = "#hotel.name + #hotel.address + #hotel.town")
    })
    public void deleteHotel(Hotel hotel) {

        Hotel deletingHotel = findHotelById(hotel.getId());
        hotelRepository.deleteById(deletingHotel.getId());
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

    @Override
    public List<Hotel> findAll() {
        return hotelRepository.findAll();
    }

    private Hotel checkAndSave(Hotel hotel) {
        lock.lock();
        if (findHotelByNameAndAddressAndTown(hotel).orElse(null) != null) {
            lock.unlock();
            throw new EntityAlreadyExistsException(
                    MessageFormat.format(AppMessages.ENTITY_ALREADY_EXISTS, "Отель", hotel.getName())
            );
        }

        Hotel savedHotel = hotelRepository.saveAndFlush(hotel);
        lock.unlock();
        return savedHotel;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Hotel checkAndSaveTransactional(Hotel hotel) {
        if (findHotelByNameAndAddressAndTown(hotel).orElse(null) != null) {
            throw new EntityAlreadyExistsException(
                    MessageFormat.format(AppMessages.ENTITY_ALREADY_EXISTS, "Отель", hotel.getName())
            );
        }
        log.info("Сохраняем отели");
        try {
            Hotel savedHotel = hotelRepository.saveAndFlush(hotel);
            return savedHotel;
        } catch (DataIntegrityViolationException exception) {
            throw new EntityAlreadyExistsException(
                    MessageFormat.format(AppMessages.ENTITY_ALREADY_EXISTS, "Отель", hotel.getName())
            );
        }

    }
    @Cacheable(value = "HotelService::findHotelByNameAndAddressAndTown", key = "#hotel.name + #hotel.address + #hotel.town")
    public Optional<Hotel> findHotelByNameAndAddressAndTown(Hotel hotel) {
        return hotelRepository.findByNameAndAddressAndTown(
                hotel.getName(),
                hotel.getAddress(),
                hotel.getTown()
        );
    }
}
