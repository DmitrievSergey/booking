package com.booking.demo.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HotelDto {
    @NotNull
    @Size(min = 3, max = 50, message = "Наименование отеля не должно быть меньше {min} символов и не должно быть больше {max} символов")
    String name;

    @NotNull
    @Size(min = 10, max = 50, message = "Заголовок отеля не должен быть меньше {min} символов и не должен быть больше {max} символов")
    String title;

    @NotNull
    @Size(min = 3, max = 50, message = "Название города не должно быть меньше {min} символов и не должно быть больше {max} символов")
    String town;

    @NotNull
    @Size(min = 3, max = 50, message = "Адрес отеля не должен быть меньше {min} символов и не должен быть больше {max} символов")
    String address;

    String distance;

}
