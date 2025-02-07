package com.example.bookingservice.dto.reservation.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestReserveRoom {
    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate startDate;
    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate endDate;


}
