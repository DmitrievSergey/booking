package com.example.statisticservice.service;


import com.example.statisticservice.entity.UserEntity;

public interface UserService {

    UserEntity findByUserName(String username);
}
