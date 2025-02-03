package com.example.bookingservice.dto.user.request;

import jakarta.validation.constraints.Email;
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
public class RequestCreateUser {

    @NotNull
    @Size(min = 3, max = 50, message = "Имя пользователя не должно быть меньше {min} символов и не должно быть больше {max} символов")
    private String name;


    @NotNull
    @Size(min = 3, max = 7, message = "Пароль пользователя не должен быть меньше {min} символов и не должен быть больше {max} символов")
    private String password;

    @NotNull
    @Email
    private String email;
}
