package com.example.bookingservice.repository;


import com.example.bookingservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmailAndUserName(String email, String name);

    Optional<User> findByUserName(String username);

}
