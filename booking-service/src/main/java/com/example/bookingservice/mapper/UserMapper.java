package com.example.bookingservice.mapper;

import com.example.bookingservice.dto.user.request.RequestCreateUser;
import com.example.bookingservice.dto.user.response.ResponseUser;
import com.example.bookingservice.model.RoleType;
import com.example.bookingservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "name", source = "user.userName")
    ResponseUser mapToResponse(User user);

    User mapToEntity(RequestCreateUser user);

    @Mapping(target = "userName", source = "user.name")
    @Mapping(target = "roles", source = "role")
    User mapToEntity(RequestCreateUser user, RoleType role);

    default String[] map(RoleType value) {
        return new String[] {value.name()};
    }
}
