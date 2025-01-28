package com.example.bookingservice.controller;


import com.example.bookingservice.dto.reservation.request.RequestReserveRoom;
import com.example.bookingservice.dto.reservation.response.ResponseReservation;
import com.example.bookingservice.mapper.ReservationMapper;
import com.example.bookingservice.model.ReservationInterval;
import com.example.bookingservice.model.User;
import com.example.bookingservice.security.AppUserDetails;
import com.example.bookingservice.service.ReservationService;
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


    @PostMapping("/{roomId}")
    ResponseEntity<ResponseReservation> reserveRoom(@PathVariable() String roomId
            , @RequestBody RequestReserveRoom reservation
    , @AuthenticationPrincipal AppUserDetails user) {
        User opUser = userService.findByUserName(user.getUsername());
        ReservationInterval reservationInterval = reservationMapper.mapToEntity(
                roomId,
                reservation,
                opUser
        );
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
