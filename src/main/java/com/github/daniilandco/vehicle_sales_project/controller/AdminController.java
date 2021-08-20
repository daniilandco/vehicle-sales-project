package com.github.daniilandco.vehicle_sales_project.controller;

import com.github.daniilandco.vehicle_sales_project.model.ad.Ad;
import com.github.daniilandco.vehicle_sales_project.model.user.User;
import com.github.daniilandco.vehicle_sales_project.repository.ad.AdRepository;
import com.github.daniilandco.vehicle_sales_project.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UserRepository userRepository;

    @Autowired
    private AdRepository adRepository;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/ads")
    public Iterable<Ad> getAllAds() {
        return adRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public User getById(@PathVariable Long id) {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false).
                filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        userRepository.save(user);
        return user;
    }

    @DeleteMapping("/users/{id}")
    public void deleteById(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}
