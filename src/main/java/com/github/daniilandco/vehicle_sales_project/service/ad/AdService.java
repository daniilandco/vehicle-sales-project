package com.github.daniilandco.vehicle_sales_project.service.ad;

import com.github.daniilandco.vehicle_sales_project.controller.request.NewAdRequest;
import com.github.daniilandco.vehicle_sales_project.dto.model.ad.AdDto;
import com.github.daniilandco.vehicle_sales_project.exception.*;

import java.net.URL;

/**
 * Created by Daniel Bondarkov.
 */
public interface AdService {

    Iterable<AdDto> getAllAds();

    AdDto newAd(NewAdRequest request) throws UserIsNotLoggedInException, CategoryException;

    Iterable<AdDto> getUserAds() throws JwtAuthenticationException, UserIsNotLoggedInException;

    void updateAd(Long id, NewAdRequest updatedAd) throws UserIsNotLoggedInException, CategoryException, AdNotFoundException;

    AdDto getAdById(Long id);

    void deleteUserAdById(Long id) throws Exception;

    AdDto getUserAdById(Long id) throws AdDoesNotBelongToLoggedInUserException, AdNotFoundException;

    void uploadAdPhotos(Long id, byte[][] images) throws Exception;

    URL getAdPhotoById(Long adId, int photoId) throws Exception;
}
