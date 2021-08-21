package com.github.daniilandco.vehicle_sales_project.service.ad;

import com.github.daniilandco.vehicle_sales_project.controller.request.NewAdRequest;
import org.springframework.http.ResponseEntity;

/**
 * Created by Daniel Bondarkov.
 */
public interface AdService {

    ResponseEntity<?> addAd(NewAdRequest request);

    ResponseEntity<?> getUserAds();

    ResponseEntity<?> updateAd(Long id, NewAdRequest updatedAd);

    ResponseEntity<?> getAdById(Long id);
}
