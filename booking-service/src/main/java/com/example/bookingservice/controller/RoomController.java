package com.example.bookingservice.controller;

import com.example.bookingservice.dto.filter.RoomFilter;
import com.example.bookingservice.dto.roomdto.request.CreateRoomDto;
import com.example.bookingservice.dto.roomdto.response.ResponseDeleteRoomDto;
import com.example.bookingservice.dto.roomdto.response.ResponseRoomDto;
import com.example.bookingservice.exception.EntityNotFoundException;
import com.example.bookingservice.mapper.RoomMapper;
import com.example.bookingservice.model.Hotel;
import com.example.bookingservice.model.Room;
import com.example.bookingservice.service.HotelService;
import com.example.bookingservice.service.RoomService;
import com.example.bookingservice.utils.AppMessages;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/room")
public class RoomController {

    private final RoomMapper roomMapper;
    private final RoomService roomService;
    private final HotelService hotelService;

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    ResponseEntity<ResponseRoomDto> createRoom(@RequestParam("hotelId") String hotelId, @Valid @RequestBody CreateRoomDto roomDto) {
        try {
            Hotel hotel = hotelService.findHotelById(hotelId);
            Room room = roomMapper.map(roomDto, hotel);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    roomMapper.map(roomService.save(room))
            );
        } catch (NoSuchElementException exception) {
            throw new EntityNotFoundException(
                    MessageFormat.format(AppMessages.ENTITY_NOT_EXISTS, "Отель", hotelId)
            );
        }

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    ResponseEntity<ResponseRoomDto> updateRoom(@PathVariable(name = "id") String roomId,
                                               @Valid @RequestBody CreateRoomDto roomDto) {
        Room room = roomMapper.map(roomDto);

        return ResponseEntity.ok(
                roomMapper.map(roomService.update(roomId, room))
        );
    }

    @GetMapping("/{id}")
    ResponseEntity<ResponseRoomDto> findRoomById(@PathVariable(name = "id") String roomId) {

        return ResponseEntity.ok(
                roomMapper.map(roomService.findRoomById(roomId))
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    ResponseEntity<ResponseDeleteRoomDto> deleteRoomById(@PathVariable(name = "id") String roomId) {
        roomService.deleteRoomById(roomId);
        return ResponseEntity.ok(new ResponseDeleteRoomDto("Комната с Id " + roomId + " удалена"));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ResponseRoomDto>> findBy(@Valid RoomFilter roomFilter) {
        return ResponseEntity.ok(
                roomService.filterBy(roomFilter).stream().map(roomMapper::map).toList()
        );
    }
}
