package com.booking.demo.controller;

import com.booking.demo.dto.filter.HotelFilter;
import com.booking.demo.dto.filter.RoomFilter;
import com.booking.demo.dto.hotel.response.ResponseHotelDto;
import com.booking.demo.dto.roomdto.request.CreateRoomDto;
import com.booking.demo.dto.roomdto.response.ResponseRoomDto;
import com.booking.demo.mapper.RoomMapper;
import com.booking.demo.model.Room;
import com.booking.demo.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/room")
public class RoomController {

    private final RoomMapper roomMapper;
    private final RoomService roomService;

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    ResponseEntity<ResponseRoomDto> createRoom(@Valid @RequestBody CreateRoomDto roomDto) {
        Room room = roomMapper.map(roomDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                roomMapper.map(roomService.save(room))
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    ResponseEntity<ResponseRoomDto> updateRoom(@PathVariable(name = "id") String roomId,
                                               @Valid @RequestBody CreateRoomDto roomDto) {
        Room room = roomMapper.map(roomDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(
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
    ResponseEntity<Void> deleteRoomById(@PathVariable(name = "id") String roomId) {
        roomService.deleteRoomById(roomId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ResponseRoomDto>> findBy(@Valid RoomFilter roomFilter) {
        return ResponseEntity.ok(
                roomService.filterBy(roomFilter).stream().map(roomMapper::map).toList()
        );
    }
}
