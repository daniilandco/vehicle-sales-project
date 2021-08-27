package com.github.daniilandco.vehicle_sales_project.controller;

import com.github.daniilandco.vehicle_sales_project.controller.request.RegisterRequest;
import com.github.daniilandco.vehicle_sales_project.controller.response.RestApiResponse;
import com.github.daniilandco.vehicle_sales_project.exception.JwtAuthenticationException;
import com.github.daniilandco.vehicle_sales_project.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestBody RegisterRequest request) {
        try {
            userService.updateProfile(request);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new RestApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "update profile error"));
        }
        return ResponseEntity
                .ok(new RestApiResponse(HttpStatus.OK.value(),
                        "profile is updated"));
    }

    @PostMapping("/profile_photo")
    public ResponseEntity<?> updateProfilePhoto(@RequestParam("file") MultipartFile imageFile) {
        try {
            userService.updateProfilePhoto(imageFile);
        } catch (IOException | JwtAuthenticationException e) {
            return ResponseEntity
                    .badRequest()
                    .body((new RestApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Update profile photo error")));
        }
        return ResponseEntity.ok(new RestApiResponse(HttpStatus.OK.value(), "profile photo is updated"));
    }

    @GetMapping("/profile_photo")
    public ResponseEntity<?> getProfilePhoto() {
        try {
            return ResponseEntity.ok(new RestApiResponse(HttpStatus.OK.value(), "", userService.getProfilePhoto()));
        } catch (IOException | JwtAuthenticationException e) {
            e.printStackTrace();
            return null;
        }
    }

}
