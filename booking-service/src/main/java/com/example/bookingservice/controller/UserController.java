package com.example.bookingservice.controller;

import com.example.bookingservice.dto.user.request.RequestCreateUser;
import com.example.bookingservice.dto.user.response.ResponseUser;
import com.example.bookingservice.mapper.UserMapper;
import com.example.bookingservice.model.RoleType;
import com.example.bookingservice.model.User;
import com.example.bookingservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserMapper userMapper;
    private final UserService userService;

    @PostMapping("/add")
    ResponseEntity<ResponseUser> createUser(@RequestParam RoleType role,
                                            @Valid @RequestBody RequestCreateUser user) {
        User newUser = userMapper.mapToEntity(user, role);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                     userMapper.mapToResponse(userService.save(newUser))
                );
    }
}
