package com.github.daniilandco.vehicle_sales_project.controller;

import com.github.daniilandco.vehicle_sales_project.controller.request.NewAdRequest;
import com.github.daniilandco.vehicle_sales_project.service.ad.AdServiceImplementation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final AdServiceImplementation adServiceImplementation;

    public UserController(AdServiceImplementation adServiceImplementation) {
        this.adServiceImplementation = adServiceImplementation;
    }

    @GetMapping("/ads")
    public ResponseEntity<?> getUserAds() {
        return adServiceImplementation.getUserAds();
    }

    @GetMapping("/ads/{id}")
    public ResponseEntity<?> getAdById(@PathVariable Long id) {
        return adServiceImplementation.getAdById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addAd(@RequestBody NewAdRequest request) {
        return adServiceImplementation.addAd(request);
    }

    @PutMapping("/ads/{id}/update")
    public ResponseEntity<?> updateAd(@PathVariable Long id, @RequestBody NewAdRequest request) {
        return adServiceImplementation.updateAd(id, request);
    }
}
