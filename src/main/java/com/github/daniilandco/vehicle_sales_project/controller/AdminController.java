package com.github.daniilandco.vehicle_sales_project.controller;

import com.github.daniilandco.vehicle_sales_project.controller.request.RegisterRequest;
import com.github.daniilandco.vehicle_sales_project.service.ad.AdServiceImplementation;
import com.github.daniilandco.vehicle_sales_project.service.user.UserServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserServiceImplementation userServiceImplementation;

    @Autowired
    private AdServiceImplementation adServiceImplementation;


    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return userServiceImplementation.getAllUsers();
    }

    @GetMapping("/ads")
    public ResponseEntity<?> getAllAds() {
        return adServiceImplementation.getAllAds();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return userServiceImplementation.getUserById(id);
    }

    @GetMapping("/ads/{id}")
    public ResponseEntity<?> getAdById(@PathVariable Long id) {
        return adServiceImplementation.getAdById(id);
    }

    @PostMapping("/users")
    public ResponseEntity<?> addUser(@RequestBody RegisterRequest request) {
        return userServiceImplementation.register(request);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userServiceImplementation.deleteUserById(id);
    }
}
