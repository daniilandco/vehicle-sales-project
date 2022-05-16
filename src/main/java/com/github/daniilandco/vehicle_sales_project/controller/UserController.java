package com.github.daniilandco.vehicle_sales_project.controller;

import com.github.daniilandco.vehicle_sales_project.dto.request.RegisterRequestDTO;
import com.github.daniilandco.vehicle_sales_project.dto.response.RestApiResponse;
import com.github.daniilandco.vehicle_sales_project.exception.auth.JwtAuthenticationException;
import com.github.daniilandco.vehicle_sales_project.exception.auth.UserIsNotLoggedInException;
import com.github.daniilandco.vehicle_sales_project.exception.image.InvalidImageSizeException;
import com.github.daniilandco.vehicle_sales_project.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping("/")
    public ResponseEntity<?> updateProfile(@RequestBody RegisterRequestDTO request) throws UserIsNotLoggedInException {
        userService.updateProfile(request);

        return ResponseEntity.ok(new RestApiResponse("profile is updated"));
    }

    @PostMapping("/profile_photo")
    public ResponseEntity<?> updateProfilePhoto(@RequestParam("file") MultipartFile imageFile) throws IOException, InvalidImageSizeException, UserIsNotLoggedInException, JwtAuthenticationException {
        userService.updateProfilePhoto(imageFile.getBytes());

        return ResponseEntity.ok(new RestApiResponse("profile photo is updated"));

    }

    @GetMapping("/profile_photo")
    public ResponseEntity<?> getProfilePhoto() throws UserIsNotLoggedInException, IOException, JwtAuthenticationException {
        return ResponseEntity.ok(new RestApiResponse(
                "profile photo of current user",
                userService.getProfilePhoto()));

    }

}
