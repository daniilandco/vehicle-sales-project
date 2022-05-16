package com.github.daniilandco.vehicle_sales_project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.daniilandco.vehicle_sales_project.dto.request.AdRequestDTO;
import com.github.daniilandco.vehicle_sales_project.dto.request.SearchByParamsRequestDTO;
import com.github.daniilandco.vehicle_sales_project.dto.request.SearchByQueryRequestDTO;
import com.github.daniilandco.vehicle_sales_project.dto.response.RestApiResponse;
import com.github.daniilandco.vehicle_sales_project.exception.ad.AdDoesNotBelongToLoggedInUserException;
import com.github.daniilandco.vehicle_sales_project.exception.ad.AdNotFoundException;
import com.github.daniilandco.vehicle_sales_project.exception.auth.JwtAuthenticationException;
import com.github.daniilandco.vehicle_sales_project.exception.auth.UserIsNotLoggedInException;
import com.github.daniilandco.vehicle_sales_project.exception.category.CategoryException;
import com.github.daniilandco.vehicle_sales_project.exception.image.AdPhotoNotFoundException;
import com.github.daniilandco.vehicle_sales_project.exception.image.InvalidImageSizeException;
import com.github.daniilandco.vehicle_sales_project.service.AdService;
import com.github.daniilandco.vehicle_sales_project.service.ImageService;
import com.github.daniilandco.vehicle_sales_project.service.SearchEngineService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("/ad")
@AllArgsConstructor
public class AdController {
    private final AdService adService;
    private final ImageService imageService;
    private final SearchEngineService searchEngineService;

    @GetMapping("/all")
    public ResponseEntity<?> getUserAds() throws UserIsNotLoggedInException, JwtAuthenticationException {
        return ResponseEntity.ok(new RestApiResponse("ok", adService.getUserAds()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAdById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(new RestApiResponse("ok", adService.getUserAdById(id)));
        } catch (AdDoesNotBelongToLoggedInUserException | AdNotFoundException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new RestApiResponse(e.getMessage()));
        }
    }

    @GetMapping("/search/params")
    public ResponseEntity<?> search(@RequestBody SearchByParamsRequestDTO request) {
        try {
            return ResponseEntity.ok(new RestApiResponse("ok", searchEngineService.search(request)));
        } catch (IOException | AdNotFoundException | CategoryException e) {
            return ResponseEntity
                    .badRequest()
                    .body((new RestApiResponse(e.getMessage())));
        }
    }

    @GetMapping("/search/query")
    public ResponseEntity<?> search(@RequestParam("request") String encodedRequest) throws IOException, AdNotFoundException {
        final SearchByQueryRequestDTO request =
                new ObjectMapper().readValue(Base64.getDecoder().decode(encodedRequest), SearchByQueryRequestDTO.class);

        return ResponseEntity.ok(new RestApiResponse("results of the search query", searchEngineService.search(request)));
    }

    @PostMapping("/new")
    public ResponseEntity<?> newAd(@RequestBody AdRequestDTO request) throws CategoryException, UserIsNotLoggedInException {
        return ResponseEntity.ok(new RestApiResponse(
                "new ad with has been registered",
                adService.newAd(request)));
    }

    @PostMapping("/{id}/upload_photos")
    public ResponseEntity<?> uploadAdPhotos(@PathVariable Long id, @RequestParam("file") MultipartFile[] images) throws IOException, InvalidImageSizeException, AdNotFoundException, UserIsNotLoggedInException {
        adService.uploadAdPhotos(id, imageService.getBytesArrayFromMultipartFileArray(images));

        return ResponseEntity.ok(new RestApiResponse("ad photos have been updated"));
    }


    @GetMapping("/{id}/main_photo")
    public ResponseEntity<?> getMainAdPhoto(@PathVariable Long id) throws AdPhotoNotFoundException {
        return ResponseEntity.ok(new RestApiResponse(
                "main photo of an ad with id=%d".formatted(id),
                adService.getAdPhotoById(id, 0)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAd(@PathVariable Long id, @RequestBody AdRequestDTO request) throws CategoryException, AdNotFoundException, UserIsNotLoggedInException {
        return ResponseEntity.ok(new RestApiResponse(
                "an ad with id=%d has been updated".formatted(id),
                adService.updateAd(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAdById(@PathVariable Long id) throws AdNotFoundException, UserIsNotLoggedInException, AdDoesNotBelongToLoggedInUserException {
        adService.deleteUserAdById(id);

        return ResponseEntity.ok(
                new RestApiResponse("an ad with id=%d is deleted".formatted(id)));
    }
}
