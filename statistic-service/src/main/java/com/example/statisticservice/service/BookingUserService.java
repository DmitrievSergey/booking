package com.example.statisticservice.service;

import com.example.statisticservice.entity.BookingUserEntity;

import java.util.List;

public interface BookingUserService {
    BookingUserEntity saveUserRegistrationEvent(BookingUserEntity user);

    List<BookingUserEntity> findAll();
}
