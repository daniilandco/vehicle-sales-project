package com.github.daniilandco.vehicle_sales_project.controller;

import com.github.daniilandco.vehicle_sales_project.controller.request.RegisterRequest;
import com.github.daniilandco.vehicle_sales_project.controller.response.RestApiResponse;
import com.github.daniilandco.vehicle_sales_project.exception.*;
import com.github.daniilandco.vehicle_sales_project.service.ad.AdService;
import com.github.daniilandco.vehicle_sales_project.service.user.UserService;
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
        return ResponseEntity.ok(new RestApiResponse("ok", userService.getAllUsers()));
    }

    @GetMapping("/ads")
    public ResponseEntity<?> getAllAds() {
        return ResponseEntity.ok(new RestApiResponse("ok", adService.getAllAds()));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return ResponseEntity
                .ok(new RestApiResponse("ok", userService.getUserById(id)));
    }

    @GetMapping("/ads/{id}")
    public ResponseEntity<?> getAdById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(new RestApiResponse("ok", adService.getAdById(id)));
        } catch (AdNotFoundException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new RestApiResponse(e.getMessage()));
        }
    }

    @PostMapping("/users")
    public ResponseEntity<?> addUser(@RequestBody RegisterRequest request) {
        try {
            userService.register(request);
            return ResponseEntity.ok(new RestApiResponse("user is registered"));
        } catch (EmailAlreadyExistsException | JwtAuthenticationException | PhoneNumberAlreadyExistsException | RegistrationException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new RestApiResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/users/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
    }
}
