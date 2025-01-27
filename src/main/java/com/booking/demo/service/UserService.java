package com.booking.demo.service;

import com.booking.demo.model.User;

public interface UserService {
    User save(User user);

    User findByUserName(String userName);
}
