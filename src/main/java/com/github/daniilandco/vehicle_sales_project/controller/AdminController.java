package com.github.daniilandco.vehicle_sales_project.controller;

import com.github.daniilandco.vehicle_sales_project.dto.request.RegisterRequestDTO;
import com.github.daniilandco.vehicle_sales_project.dto.response.RestApiResponse;
import com.github.daniilandco.vehicle_sales_project.exception.ad.AdNotFoundException;
import com.github.daniilandco.vehicle_sales_project.exception.auth.*;
import com.github.daniilandco.vehicle_sales_project.service.AdService;
import com.github.daniilandco.vehicle_sales_project.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final AdService adService;

    public AdminController(UserService userService, AdService adService) {
        this.userService = userService;
        this.adService = adService;
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(new RestApiResponse("list of all users", userService.getAllUsers()));
    }

    @GetMapping("/ads")
    public ResponseEntity<?> getAllAds() {
        return ResponseEntity.ok(new RestApiResponse(
                "list of all ads",
                adService.getAllAds()));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return ResponseEntity
                .ok(new RestApiResponse(
                        "user with id=%d".formatted(id),
                        userService.getUserById(id)));
    }

    @GetMapping("/ads/{id}")
    public ResponseEntity<?> getAdById(@PathVariable Long id) throws AdNotFoundException {
        return ResponseEntity.ok(new RestApiResponse(
                "ad with id=%d".formatted(id),
                adService.getAdById(id)));
    }

    @PostMapping("/users")
    public ResponseEntity<?> addUser(@RequestBody RegisterRequestDTO request) throws RegistrationException, EmailAlreadyExistsException, UserIsNotLoggedInException, JwtAuthenticationException, PhoneNumberAlreadyExistsException {
        return ResponseEntity.ok(new RestApiResponse(
                "new user has been registered",
                userService.register(request)));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return null;
    }
}
