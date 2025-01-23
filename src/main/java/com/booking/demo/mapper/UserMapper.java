package com.booking.demo.mapper;

import com.booking.demo.dto.user.request.RequestCreateUser;
import com.booking.demo.dto.user.response.ResponseUser;
import com.booking.demo.model.RoleType;
import com.booking.demo.model.User;
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
