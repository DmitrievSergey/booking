package com.example.bookingservice.dto.roomdto.request;


import com.example.bookingservice.dto.hotel.request.HotelDto;
import com.example.bookingservice.model.Hotel;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateRoomDto {
    @NotNull
    @Size(min = 3, max = 50, message = "Наименование комнаты не должно быть меньше {min} символов и не должно быть больше {max} символов")
    String name;

    @NotNull
    @Size(min = 3, max = 50, message = "Описание комнаты не должно быть меньше {min} символов и не должно быть больше {max} символов")
    String description;

    @NotNull
    @Size(min = 1, max = 5, message = "Номер комнаты не должен быть меньше {min} символов и не должно быть больше {max} символов")
    String number;

    @Positive
    Float pricePerDay;

    @Min(value = 1, message = "Количество людей в комнате должно быть больше или равно {value}")
    Byte peopleCount;

    @NotNull
    Hotel hotel;
}
