package com.github.daniilandco.vehicle_sales_project.controller;

import com.github.daniilandco.vehicle_sales_project.controller.request.NewAdRequest;
import com.github.daniilandco.vehicle_sales_project.controller.request.SearchByParamsRequest;
import com.github.daniilandco.vehicle_sales_project.controller.response.RestApiResponse;
import com.github.daniilandco.vehicle_sales_project.exception.*;
import com.github.daniilandco.vehicle_sales_project.service.ad.AdService;
import com.github.daniilandco.vehicle_sales_project.service.search.SearchEngineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/ad")
public class AdController {

    private final AdService adService;

    private final SearchEngineService searchEngineService;

    public AdController(AdService adService, SearchEngineService searchEngineService) {
        this.adService = adService;
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

    @GetMapping("/search_request")
    public ResponseEntity<?> getAdsByParams(@RequestBody SearchByParamsRequest request) {
        try {
            return ResponseEntity.ok(new RestApiResponse("ok", searchEngineService.getAdsByParams(request)));
        } catch (CategoryException e) {
            return ResponseEntity
                    .badRequest()
                    .body((new RestApiResponse(e.getMessage())));
        }
    }

    @GetMapping("/search_query")
    public ResponseEntity<?> getAdsByQuery(@RequestBody String query) {
        return ResponseEntity.ok(new RestApiResponse("ok", searchEngineService.getAdsByQuery(query)));
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
            adService.uploadAdPhotos(id, getBytesArrayFromMultipartFileArray(images));
            return ResponseEntity.ok(new RestApiResponse("ad photos are updated"));
        } catch (IOException | AdNotFoundException | UserIsNotLoggedInException e) {
            return ResponseEntity
                    .badRequest()
                    .body((new RestApiResponse(e.getMessage())));
        }
    }

    private byte[][] getBytesArrayFromMultipartFileArray(MultipartFile[] images) throws IOException {
        byte[][] bytesArray = new byte[images.length][];
        for (int i = 0; i < images.length; ++i) {
            bytesArray[i] = images[i].getBytes();
        }
        return bytesArray;
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

    @PutMapping("/{id}/update")
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

    @DeleteMapping("/{id}/delete")
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
