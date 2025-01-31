package com.example.statisticservice.repository;


import com.example.statisticservice.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserEntity, String> {

}
