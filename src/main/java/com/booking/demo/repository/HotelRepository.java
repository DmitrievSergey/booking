package com.booking.demo.repository;

import com.booking.demo.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface HotelRepository extends JpaRepository<Hotel, String>, JpaSpecificationExecutor<Hotel>
        , PagingAndSortingRepository<Hotel, String> {

    Optional<Hotel> findByNameAndAddressAndTown(String hotelName, String hotelAddress
    , String hotelTown);
}
