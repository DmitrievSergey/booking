package com.example.bookingservice.controller;

import com.example.basedomain.UserRegistration;
import com.example.basedomain.UserRegistrationEvent;
import com.example.bookingservice.dto.user.request.RequestCreateUser;
import com.example.bookingservice.dto.user.response.ResponseUser;
import com.example.bookingservice.mapper.UserMapper;
import com.example.bookingservice.model.RoleType;
import com.example.bookingservice.model.User;
import com.example.bookingservice.service.KafkaMessagePublisher;
import com.example.bookingservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserMapper userMapper;
    private final UserService userService;
    private final KafkaMessagePublisher kafkaMessagePublisher;

    @PostMapping("/add")
    ResponseEntity<ResponseUser> createUser(@RequestParam RoleType role,
                                            @Valid @RequestBody RequestCreateUser user) {
        User newUser = userMapper.mapToEntity(user, role);
        UserRegistrationEvent event = new UserRegistrationEvent();
        event.setUserRegistration(new UserRegistration(newUser.getId()));
        kafkaMessagePublisher.sendUserRegistrationEvent(event);
        log.info("Message sent to kafka");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                     userMapper.mapToResponse(userService.save(newUser))
                );
    }
}
