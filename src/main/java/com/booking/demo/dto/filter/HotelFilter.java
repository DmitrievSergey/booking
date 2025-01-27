package com.booking.demo.dto.filter;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class HotelFilter {

    @Min(value = 1, message = "Количество записей на странице должно быть {value} или больше {value}")
    private int pageSize;
    @Min(value = 0, message = "Номер страницы должен быть {value} или больше  {value}")
    private int pageNumber;

    private String id;

    private String name;

    private String title;

    private String town;

    private String address;

    private String distance;

    private Float rating = 0.0f;

    private Integer numberOfRating;
}
