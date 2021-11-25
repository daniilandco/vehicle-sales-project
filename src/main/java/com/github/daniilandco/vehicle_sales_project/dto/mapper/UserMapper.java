package com.github.daniilandco.vehicle_sales_project.dto.mapper;

import com.github.daniilandco.vehicle_sales_project.dto.model.user.UserDto;
import com.github.daniilandco.vehicle_sales_project.model.user.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {

    private final AdMapper adMapper;

    public UserMapper(AdMapper adMapper) {
        this.adMapper = adMapper;
    }

    public Iterable<UserDto> toUserDtoList(Iterable<User> userList) {
        List<UserDto> userDtoList = new ArrayList<>();
        userList.forEach(user -> userDtoList.add(toUserDto(user)));
        return userDtoList;
    }

    public UserDto toUserDto(User user) {
        return new UserDto()
                .setId(user.getId())
                .setEmail(user.getEmail())
                .setFirstName(user.getFirstName())
                .setSecondName(user.getSecondName())
                .setPhoneNumber(user.getPhoneNumber())
                .setLocation(user.getLocation())
                .setLastLogin(user.getLastLogin())
                .setRegisteredAt(user.getRegisteredAt())
                .setStatus(user.getStatus())
                .setRole(user.getRole());
    }
}
