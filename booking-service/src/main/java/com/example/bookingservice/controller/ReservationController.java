package com.example.bookingservice.controller;


import com.example.basedomain.RoomReservation;
import com.example.basedomain.RoomReservationEvent;
import com.example.bookingservice.dto.reservation.request.RequestReserveRoom;
import com.example.bookingservice.dto.reservation.response.ResponseReservation;
import com.example.bookingservice.mapper.ReservationMapper;
import com.example.bookingservice.model.ReservationInterval;
import com.example.bookingservice.model.Room;
import com.example.bookingservice.model.User;
import com.example.bookingservice.security.AppUserDetails;
import com.example.bookingservice.service.KafkaMessagePublisher;
import com.example.bookingservice.service.ReservationService;
import com.example.bookingservice.service.RoomService;
import com.example.bookingservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/reservation")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;
    private final UserService userService;
    private final RoomService roomService;
    private final KafkaMessagePublisher kafkaMessagePublisher;


    @PostMapping("/{roomId}")
    ResponseEntity<ResponseReservation> reserveRoom(@PathVariable() String roomId
            , @RequestBody RequestReserveRoom reservation
    , @AuthenticationPrincipal AppUserDetails user) {
        User opUser = userService.findByUserName(user.getUsername());
        Room room = roomService.findRoomById(roomId);
        ReservationInterval reservationInterval = reservationMapper.mapToEntity(
                room,
                reservation,
                opUser
        );
        //log.info("Reservation Interval {}",reservationInterval.toString());
        RoomReservationEvent event = new RoomReservationEvent();
        event.setRoomReservation(new RoomReservation(
                opUser.getId(),
                roomId,
                reservation.getStartDate()
                , reservation.getEndDate()));
        kafkaMessagePublisher.sendRoomReservationEvent(event);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        reservationMapper.mapToResponse(reservationService.save(reservationInterval))
                );
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    ResponseEntity<List<ResponseReservation>> findAllReservations() {
        return ResponseEntity.ok(
                reservationService.findAllReservations().stream()
                        .map(reservationMapper::mapToResponse).toList()
        );
    }
}
