package com.github.daniilandco.vehicle_sales_project.controller;

import com.github.daniilandco.vehicle_sales_project.model.user.User;
import com.github.daniilandco.vehicle_sales_project.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/")
public class AdminController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('manage')")
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasAuthority('manage')")
    public User getById(@PathVariable Long id) {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false).
                filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @PostMapping("/users")
    @PreAuthorize("hasAuthority('manage')")
    public User addUser(@RequestBody User user) {
        userRepository.save(user);
        return user;
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasAuthority('manage')")
    public void deleteById(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}
