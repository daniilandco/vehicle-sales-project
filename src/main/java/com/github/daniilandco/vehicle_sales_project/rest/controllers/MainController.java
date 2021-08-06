package com.github.daniilandco.vehicle_sales_project.rest.controllers;

import com.github.daniilandco.vehicle_sales_project.database_access.ad.Ad;
import com.github.daniilandco.vehicle_sales_project.database_access.ad.AdRepository;
import com.github.daniilandco.vehicle_sales_project.database_access.user.User;
import com.github.daniilandco.vehicle_sales_project.database_access.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api")
public class MainController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('users:write')")
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasAuthority('users:write')")
    public User getById(@PathVariable Long id) {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false).
                filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @PostMapping("/users")
    @PreAuthorize("hasAuthority('users:write')")
    public User addUser(@RequestBody User user) {
        userRepository.save(user);
        return user;
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasAuthority('users:write')")
    public void deleteById(@PathVariable Long id) {
        userRepository.deleteById(id);
    }


}