package com.booking.demo.controller;

import com.booking.demo.dto.request.HotelDto;
import com.booking.demo.dto.response.ResponseFindHotelById;
import com.booking.demo.dto.response.ResponseHotelDto;
import com.booking.demo.mapper.HotelMapper;
import com.booking.demo.model.Hotel;
import com.booking.demo.service.HotelService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
