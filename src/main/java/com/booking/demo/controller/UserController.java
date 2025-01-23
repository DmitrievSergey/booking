package com.booking.demo.controller;

import com.booking.demo.dto.user.request.RequestCreateUser;
import com.booking.demo.dto.user.response.ResponseUser;
import com.booking.demo.mapper.UserMapper;
import com.booking.demo.model.RoleType;
import com.booking.demo.model.User;
import com.booking.demo.service.UserService;
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
