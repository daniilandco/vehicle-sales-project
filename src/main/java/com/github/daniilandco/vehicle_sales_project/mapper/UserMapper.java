package com.github.daniilandco.vehicle_sales_project.mapper;

import com.github.daniilandco.vehicle_sales_project.dto.model.UserDTO;
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

    public Iterable<UserDTO> toUserDtoList(Iterable<User> userList) {
        List<UserDTO> userDTOList = new ArrayList<>();
        userList.forEach(user -> userDTOList.add(toUserDto(user)));
        return userDTOList;
    }

    public UserDTO toUserDto(User user) {
        return new UserDTO()
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
