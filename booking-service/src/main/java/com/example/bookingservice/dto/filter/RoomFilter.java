package com.example.bookingservice.dto.filter;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RoomFilter {
    @Min(value = 1, message = "Количество записей на странице должно быть {value} или больше {value}")
    private int pageSize;
    @Min(value = 0, message = "Номер страницы должен быть {value} или больше  {value}")
    private int pageNumber;

    private String id;

    @Size(min = 3, max = 50, message = "Наименование комнаты не должно быть меньше {min} символов и не должно быть больше {max} символов")
    private String name;


    @Size(min = 3, max = 50, message = "Описание комнаты не должно быть меньше {min} символов и не должно быть больше {max} символов")
    private String description;


    @Size(min = 1, max = 5, message = "Номер комнаты не должен быть меньше {min} символов и не должно быть больше {max} символов")
    private String number;

    @Positive
    private Float maxCost;

    @Positive
    private Float minCost;

    @Min(value = 1, message = "Количество людей в комнате должно быть больше или равно {value}")
    private Byte peopleCount;

    private LocalDate startDate;

    private LocalDate endDate;


    private String hotelId;

}
