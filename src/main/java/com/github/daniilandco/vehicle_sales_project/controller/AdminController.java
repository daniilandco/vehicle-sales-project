package com.github.daniilandco.vehicle_sales_project.controller;

import com.github.daniilandco.vehicle_sales_project.dto.mapper.AdMapper;
import com.github.daniilandco.vehicle_sales_project.dto.mapper.UserMapper;
import com.github.daniilandco.vehicle_sales_project.dto.model.ad.AdDto;
import com.github.daniilandco.vehicle_sales_project.dto.model.user.UserDto;
import com.github.daniilandco.vehicle_sales_project.model.user.User;
import com.github.daniilandco.vehicle_sales_project.repository.ad.AdRepository;
import com.github.daniilandco.vehicle_sales_project.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdRepository adRepository;


    @GetMapping("/users")
    public Iterable<UserDto> getAllUsers() {
        return UserMapper.toUserDtoSet(userRepository.findAll());
    }

    @GetMapping("/ads")
    public Iterable<AdDto> getAllAds() {
        return AdMapper.toAdDtoSet(adRepository.findAll());
    }

    @GetMapping("/users/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return UserMapper.toUserDto(Objects.requireNonNull(StreamSupport.stream(userRepository.findAll().spliterator(), false).
                filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null)));
    }

    @GetMapping("/ads/{id}")
    public AdDto getAdById(@PathVariable Long id) {
        return AdMapper.toAdDto(Objects.requireNonNull(StreamSupport.stream(adRepository.findAll().spliterator(), false).
                filter(ad -> ad.getId().equals(id))
                .findFirst()
                .orElse(null)));
    }

    @PostMapping("/users")
    public UserDto addUser(@RequestBody User user) {
        userRepository.save(user);
        return UserMapper.toUserDto(user);
    }

    @DeleteMapping("/users/{id}")
    public void deleteById(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}
