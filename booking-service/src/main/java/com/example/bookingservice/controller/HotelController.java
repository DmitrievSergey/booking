package com.example.bookingservice.controller;

import com.example.bookingservice.dto.filter.HotelFilter;
import com.example.bookingservice.dto.hotel.request.HotelDto;
import com.example.bookingservice.dto.hotel.response.ResponseFindHotelById;
import com.example.bookingservice.dto.hotel.response.ResponseHotelDto;
import com.example.bookingservice.mapper.HotelMapper;
import com.example.bookingservice.model.Hotel;
import com.example.bookingservice.service.HotelService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/hotel")
@RequiredArgsConstructor
public class HotelController {

    private final HotelMapper hotelMapper;
    private final HotelService hotelService;

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    ResponseEntity<ResponseHotelDto> createHotel(@Valid @RequestBody HotelDto request) {
        Hotel hotel = hotelMapper.map(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                hotelMapper.mapToResponse(hotelService.save(hotel))
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    ResponseEntity<ResponseHotelDto> updateHotelById(@PathVariable(name = "id") String hotelId, @Valid @RequestBody HotelDto request) {
        Hotel updatingHotel = hotelMapper.map(hotelId, request);

        return ResponseEntity.ok(
                hotelMapper.mapToResponse(hotelService.update(updatingHotel))
        );
    }

    @GetMapping("/{id}")
    ResponseEntity<ResponseFindHotelById> getHotelById(@PathVariable(name = "id") String hotelId) {

        return ResponseEntity.ok(hotelMapper.mapToResponseWithRooms(hotelService.findHotelById(hotelId)));
    }

    @GetMapping()
    ResponseEntity<List<ResponseHotelDto>> getAllHotels() {
        return ResponseEntity.ok(hotelService.getAllHotels()
                .stream().map(hotelMapper::mapToResponse).toList());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    ResponseEntity<Void> deleteHotel(@PathVariable(name = "id") String hotelId) {
        hotelService.deleteHotelById(hotelId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/rate/{hotelId}")
    ResponseEntity<ResponseHotelDto> rateHotel(@PathVariable("hotelId") String hotelId,
                                               @RequestParam("rate")
                                               @Min(value = 1, message = "Минимальное значени оценки {value}")
                                               @Max(value = 5, message = "Максимальное значени оценки {value}")
                                                       int rate) {
        Hotel ratedHotel = hotelService.rateHotel(rate, hotelId);
        return ResponseEntity.ok(hotelMapper.mapToResponse(ratedHotel));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ResponseHotelDto>> findBy(@Valid HotelFilter hotelFilter) {
        log.info(" Filter by name {}", hotelFilter.getName());
        log.info(" Filter address {}", hotelFilter.getAddress());
        return ResponseEntity.ok(
                hotelService.filterBy(hotelFilter).stream().map(hotelMapper::mapToResponse).toList()
        );
    }
}
