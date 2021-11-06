package com.github.daniilandco.vehicle_sales_project.service.ad;

import com.github.daniilandco.vehicle_sales_project.controller.request.NewAdRequest;
import com.github.daniilandco.vehicle_sales_project.dto.model.ad.AdDto;
import com.github.daniilandco.vehicle_sales_project.exception.ad.AdDoesNotBelongToLoggedInUserException;
import com.github.daniilandco.vehicle_sales_project.exception.ad.AdNotFoundException;
import com.github.daniilandco.vehicle_sales_project.exception.auth.JwtAuthenticationException;
import com.github.daniilandco.vehicle_sales_project.exception.auth.UserIsNotLoggedInException;
import com.github.daniilandco.vehicle_sales_project.exception.category.CategoryException;
import com.github.daniilandco.vehicle_sales_project.exception.image.AdPhotoNotFoundException;
import com.github.daniilandco.vehicle_sales_project.exception.image.InvalidImageSizeException;
import com.github.daniilandco.vehicle_sales_project.model.ad.Ad;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Daniel Bondarkov.
 */
public interface AdService {

    Iterable<AdDto> getAllAds();

    AdDto newAd(NewAdRequest request) throws UserIsNotLoggedInException, CategoryException;

    Iterable<AdDto> getUserAds() throws JwtAuthenticationException, UserIsNotLoggedInException;

    void updateAd(Long id, NewAdRequest updatedAd) throws UserIsNotLoggedInException, CategoryException, AdNotFoundException;

    AdDto getAdById(Long id) throws AdNotFoundException;

    void deleteUserAdById(Long id) throws AdNotFoundException, UserIsNotLoggedInException, AdDoesNotBelongToLoggedInUserException;

    Ad getAdModelById(Long id) throws AdNotFoundException;

    AdDto getUserAdById(Long id) throws AdDoesNotBelongToLoggedInUserException, AdNotFoundException;

    void uploadAdPhotos(Long id, byte[][] images) throws UserIsNotLoggedInException, AdNotFoundException, IOException, InvalidImageSizeException;

    URL getAdPhotoById(Long adId, int photoId) throws AdPhotoNotFoundException;
}
