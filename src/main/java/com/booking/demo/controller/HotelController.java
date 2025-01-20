package com.booking.demo.controller;

import com.booking.demo.dto.request.HotelDto;
import com.booking.demo.dto.response.ResponseFindHotelById;
import com.booking.demo.dto.response.ResponseHotelDto;
import com.booking.demo.mapper.HotelMapper;
import com.booking.demo.model.Hotel;
import com.booking.demo.service.HotelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hotel")
@RequiredArgsConstructor
public class HotelController {

    private final HotelMapper hotelMapper;
    private final HotelService hotelService;

    @PostMapping("/add")
    ResponseEntity<ResponseHotelDto> createHotel(@Valid @RequestBody HotelDto request) {
        Hotel hotel = hotelMapper.map(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
            hotelMapper.mapToResponse(hotelService.save(hotel))
        );
    }

    @PutMapping("/{id}")
    ResponseEntity<ResponseHotelDto> updateHotelById(@PathVariable(name = "id") String hotelId, @Valid @RequestBody HotelDto request) {
        Hotel updatingHotel = hotelMapper.map(hotelId,request);

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
    ResponseEntity<Void> deleteHotel(@PathVariable(name = "id") String hotelId) {
        hotelService.deleteHotelById(hotelId);
        return ResponseEntity.noContent().build();
    }
}
