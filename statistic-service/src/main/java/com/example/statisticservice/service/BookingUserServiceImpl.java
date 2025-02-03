package com.example.statisticservice.service;

import com.example.statisticservice.entity.BookingUserEntity;
import com.example.statisticservice.repository.BookingUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingUserServiceImpl implements BookingUserService {

    private final BookingUserRepository userRepository;

    @Override
    public BookingUserEntity saveUserRegistrationEvent(BookingUserEntity user) {

       return  userRepository.save(user);

    }

    @Override
    public List<BookingUserEntity> findAll() {
        return userRepository.findAll();
    }
}
