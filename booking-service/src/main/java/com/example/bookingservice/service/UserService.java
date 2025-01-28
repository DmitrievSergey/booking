package com.example.bookingservice.service;


import com.example.bookingservice.model.User;

public interface UserService {
    User save(User user);

    User findByUserName(String userName);
}
