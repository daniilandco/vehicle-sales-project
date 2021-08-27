package com.github.daniilandco.vehicle_sales_project.dto.mapper;

import com.github.daniilandco.vehicle_sales_project.dto.model.user.UserDto;
import com.github.daniilandco.vehicle_sales_project.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;

@Component
public class UserMapper {

    @Autowired
    private AdMapper adMapper;

    public Iterable<UserDto> toUserDtoSet(Iterable<User> userSet) {
        Collection<UserDto> userDtoSet = new HashSet<>();
        for (User user : userSet) {
            userDtoSet.add(toUserDto(user));
        }
        return userDtoSet;
    }

    public UserDto toUserDto(User user) {
        return new UserDto()
                .setEmail(user.getEmail())
                .setFirstName(user.getFirstName())
                .setSecondName(user.getSecondName())
                .setPhoneNumber(user.getPhoneNumber())
                .setLocation(user.getLocation())
                .setLastLogin(user.getLastLogin())
                .setRegisteredAt(user.getRegisteredAt())
                .setStatus(user.getStatus())
                .setRole(user.getRole())
                .setAds(adMapper.toAdDtoSet(user.getAds()));
    }
}
