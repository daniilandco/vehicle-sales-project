package com.github.daniilandco.vehicle_sales_project.controller;

import com.github.daniilandco.vehicle_sales_project.controller.request.NewAdRequest;
import com.github.daniilandco.vehicle_sales_project.controller.request.RegisterRequest;
import com.github.daniilandco.vehicle_sales_project.service.ad.AdServiceImplementation;
import com.github.daniilandco.vehicle_sales_project.service.user.UserServiceImplementation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/user")
public class UserController {

    private final AdServiceImplementation adServiceImplementation;
    private final UserServiceImplementation userServiceImplementation;

    public UserController(AdServiceImplementation adServiceImplementation, UserServiceImplementation userServiceImplementation) {
        this.adServiceImplementation = adServiceImplementation;
        this.userServiceImplementation = userServiceImplementation;
    }

    @GetMapping("/ads")
    public ResponseEntity<?> getUserAds() {
        return adServiceImplementation.getUserAds();
    }

    @GetMapping("/ads/{id}")
    public ResponseEntity<?> getAdById(@PathVariable Long id) {
        return adServiceImplementation.getUserAdById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addAd(@RequestBody NewAdRequest request) {
        return adServiceImplementation.addAd(request);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestBody RegisterRequest request) {
        return userServiceImplementation.updateProfile(request);
    }

    @PostMapping("/profile_photo")
    public ResponseEntity<?> updateProfilePhoto(@RequestParam("file") MultipartFile imageFile) {
        try {
            return userServiceImplementation.updateProfilePhoto(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/profile_photo")
    public ResponseEntity<?> getProfilePhoto() {
        try {
            return userServiceImplementation.getProfilePhoto();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @PutMapping("/ads/{id}/update")
    public ResponseEntity<?> updateAd(@PathVariable Long id, @RequestBody NewAdRequest request) {
        return adServiceImplementation.updateAd(id, request);
    }

    @DeleteMapping("/ads/{id}/delete")
    public ResponseEntity<?> deleteAdById(@PathVariable Long id) {
        return adServiceImplementation.deleteUserAdById(id);
    }
}
