package com.github.daniilandco.vehicle_sales_project.controller;

import com.github.daniilandco.vehicle_sales_project.controller.request.NewAdRequest;
import com.github.daniilandco.vehicle_sales_project.controller.request.SearchByParamsRequest;
import com.github.daniilandco.vehicle_sales_project.controller.request.SearchByQueryRequest;
import com.github.daniilandco.vehicle_sales_project.controller.response.RestApiResponse;
import com.github.daniilandco.vehicle_sales_project.exception.ad.AdDoesNotBelongToLoggedInUserException;
import com.github.daniilandco.vehicle_sales_project.exception.ad.AdNotFoundException;
import com.github.daniilandco.vehicle_sales_project.exception.auth.JwtAuthenticationException;
import com.github.daniilandco.vehicle_sales_project.exception.auth.UserIsNotLoggedInException;
import com.github.daniilandco.vehicle_sales_project.exception.category.CategoryException;
import com.github.daniilandco.vehicle_sales_project.exception.image.InvalidImageSizeException;
import com.github.daniilandco.vehicle_sales_project.service.ad.AdService;
import com.github.daniilandco.vehicle_sales_project.service.image.ImageService;
import com.github.daniilandco.vehicle_sales_project.service.search.SearchEngineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/ad")
public class AdController {

    private final AdService adService;
    private final ImageService imageService;
    private final SearchEngineService searchEngineService;

    public AdController(AdService adService, ImageService imageService, SearchEngineService searchEngineService) {
        this.adService = adService;
        this.imageService = imageService;
        this.searchEngineService = searchEngineService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getUserAds() {
        try {
            return ResponseEntity.ok(new RestApiResponse("ok", adService.getUserAds()));
        } catch (JwtAuthenticationException | UserIsNotLoggedInException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new RestApiResponse(e.getMessage()));
        }
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
    public ResponseEntity<?> search(@RequestBody SearchByParamsRequest request) {
        try {
            return ResponseEntity.ok(new RestApiResponse("ok", searchEngineService.search(request)));
        } catch (IOException | AdNotFoundException | CategoryException e) {
            return ResponseEntity
                    .badRequest()
                    .body((new RestApiResponse(e.getMessage())));
        }
    }

    @GetMapping("/search/query")
    public ResponseEntity<?> search(@RequestBody SearchByQueryRequest request) {
        try {
            return ResponseEntity.ok(new RestApiResponse("ok", searchEngineService.search(request)));
        } catch (IOException | AdNotFoundException e) {
            return ResponseEntity
                    .badRequest()
                    .body((new RestApiResponse(e.getMessage())));
        }
    }

    @PostMapping("/new")
    public ResponseEntity<?> newAd(@RequestBody NewAdRequest request) {
        try {
            return ResponseEntity.ok(new RestApiResponse("ok", adService.newAd(request)));
        } catch (CategoryException | UserIsNotLoggedInException e) {
            return ResponseEntity
                    .badRequest()
                    .body((new RestApiResponse(e.getMessage())));
        }
    }

    @PostMapping("/{id}/upload_photos")
    public ResponseEntity<?> uploadAdPhotos(@PathVariable Long id, @RequestParam("file") MultipartFile[] images) {
        try {
            adService.uploadAdPhotos(id, imageService.getBytesArrayFromMultipartFileArray(images));
            return ResponseEntity.ok(new RestApiResponse("ad photos are updated"));
        } catch (IOException | AdNotFoundException | UserIsNotLoggedInException | InvalidImageSizeException e) {
            return ResponseEntity
                    .badRequest()
                    .body((new RestApiResponse(e.getMessage())));
        }
    }


    @GetMapping("/{id}/main_photo")
    public ResponseEntity<?> getMainAdPhoto(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(new RestApiResponse("ok", adService.getAdPhotoById(id, 0)));
        } catch (AdNotFoundException | UserIsNotLoggedInException | AdDoesNotBelongToLoggedInUserException e) {
            return ResponseEntity
                    .badRequest()
                    .body((new RestApiResponse(e.getMessage())));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAd(@PathVariable Long id, @RequestBody NewAdRequest request) {
        try {
            adService.updateAd(id, request);
            return ResponseEntity.ok(new RestApiResponse("Ad has been updated"));
        } catch (CategoryException | AdNotFoundException | UserIsNotLoggedInException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new RestApiResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAdById(@PathVariable Long id) {
        try {
            adService.deleteUserAdById(id);
            return ResponseEntity.ok(
                    new RestApiResponse("ad is deleted"));
        } catch (AdNotFoundException | UserIsNotLoggedInException | AdDoesNotBelongToLoggedInUserException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new RestApiResponse(e.getMessage()));
        }
    }
}
