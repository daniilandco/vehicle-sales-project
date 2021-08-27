package com.github.daniilandco.vehicle_sales_project.controller;

import com.github.daniilandco.vehicle_sales_project.controller.request.NewAdRequest;
import com.github.daniilandco.vehicle_sales_project.controller.response.RestApiResponse;
import com.github.daniilandco.vehicle_sales_project.exception.JwtAuthenticationException;
import com.github.daniilandco.vehicle_sales_project.service.ad.AdService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/ad")
public class AdController {

    private final AdService adService;

    public AdController(AdService adService) {
        this.adService = adService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getUserAds() {
        try {
            return ResponseEntity.ok(new RestApiResponse(HttpStatus.OK.value(), "ok", adService.getUserAds()));
        } catch (JwtAuthenticationException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new RestApiResponse(HttpStatus.OK.value(), e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAdById(@PathVariable Long id) {
        return ResponseEntity.ok(new RestApiResponse(HttpStatus.OK.value(), "ok", adService.getUserAdById(id)));
    }

    @PostMapping("/new")
    public ResponseEntity<?> newAd(@RequestBody NewAdRequest request) {
        try {
            return ResponseEntity.ok(new RestApiResponse(HttpStatus.OK.value(), "ok", adService.newAd(request)));
        } catch (JwtAuthenticationException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new RestApiResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    @PostMapping("/{id}/upload_photos")
    public ResponseEntity<?> uploadAdPhotos(@PathVariable Long id, @RequestParam("file") MultipartFile[] images) {
        try {
            adService.uploadAdPhotos(id, images);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body((new RestApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage())));
        }
        return ResponseEntity.ok(new RestApiResponse(HttpStatus.OK.value(), "ad photos are updated"));
    }

    @GetMapping("/{id}/main_photo")
    public ResponseEntity<?> getMainAdPhoto(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(new RestApiResponse(HttpStatus.OK.value(), "ok", adService.getAdPhotoById(id, 0)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body((new RestApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage())));
        }
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateAd(@PathVariable Long id, @RequestBody NewAdRequest request) {
        try {
            adService.updateAd(id, request);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new RestApiResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
        return ResponseEntity.ok(
                new RestApiResponse(HttpStatus.OK.value(), "Ad has been updated"));
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteAdById(@PathVariable Long id) {
        try {
            adService.deleteUserAdById(id);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new RestApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
        return ResponseEntity.ok(
                new RestApiResponse(HttpStatus.OK.value(), "Ad is deleted"));

    }
}
