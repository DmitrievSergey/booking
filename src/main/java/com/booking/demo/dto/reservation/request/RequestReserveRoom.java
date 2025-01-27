package com.booking.demo.dto.reservation.request;

import com.booking.demo.model.Room;
import com.booking.demo.model.User;
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

    @NotNull
    private Room room;

}
