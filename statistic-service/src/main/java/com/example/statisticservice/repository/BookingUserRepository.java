package com.example.statisticservice.repository;

import com.example.statisticservice.entity.BookingUserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookingUserRepository extends MongoRepository<BookingUserEntity, String> {
}
