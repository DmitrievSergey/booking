package com.example.statisticservice.controller;

import com.example.statisticservice.cvs.CsvGeneratorUtil;
import com.example.statisticservice.entity.BookingReservationEntity;
import com.example.statisticservice.entity.BookingUserEntity;
import com.example.statisticservice.repository.BookingUserRepository;
import com.example.statisticservice.service.BookingReservationService;
import com.example.statisticservice.service.BookingUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/statistic")
@RequiredArgsConstructor
public class FileStatisticController {

    private final BookingUserService userService;
    private final BookingReservationService reservationService;
    private final CsvGeneratorUtil csvGeneratorUtil;

    @GetMapping("/user/csv")
    public ResponseEntity<String> generateUserCsvFile() {
        List<BookingUserEntity> users = userService.findAll();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "users.csv");

        String csvBytes = csvGeneratorUtil.generateCsv(users);

        return new ResponseEntity(csvBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/reservation/csv")
    public ResponseEntity<String> generateReservationCsvFile() {
        List<BookingReservationEntity> reservations = reservationService.findAll();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "reservation.csv");

        String csvBytes = csvGeneratorUtil.generateCsv(reservations);
        log.info("csvBytes {}", csvBytes);

        return new ResponseEntity(csvBytes, headers, HttpStatus.OK);
    }
}
