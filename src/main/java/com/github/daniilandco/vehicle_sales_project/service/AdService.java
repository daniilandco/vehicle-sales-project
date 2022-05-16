package com.github.daniilandco.vehicle_sales_project.service;

import com.github.daniilandco.vehicle_sales_project.dto.model.AdDTO;
import com.github.daniilandco.vehicle_sales_project.dto.request.AdRequestDTO;
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

    Iterable<AdDTO> getAllAds();

    AdDTO newAd(AdRequestDTO request) throws UserIsNotLoggedInException, CategoryException;

    Iterable<AdDTO> getUserAds() throws JwtAuthenticationException, UserIsNotLoggedInException;

    AdDTO updateAd(Long id, AdRequestDTO updatedAd) throws UserIsNotLoggedInException, CategoryException, AdNotFoundException;

    AdDTO getAdById(Long id) throws AdNotFoundException;

    void deleteUserAdById(Long id) throws AdNotFoundException, UserIsNotLoggedInException, AdDoesNotBelongToLoggedInUserException;

    Ad getAdModelById(Long id) throws AdNotFoundException;

    AdDTO getUserAdById(Long id) throws AdDoesNotBelongToLoggedInUserException, AdNotFoundException;

    void uploadAdPhotos(Long id, byte[][] images) throws UserIsNotLoggedInException, AdNotFoundException, IOException, InvalidImageSizeException;

    URL getAdPhotoById(Long adId, int photoId) throws AdPhotoNotFoundException;
}
