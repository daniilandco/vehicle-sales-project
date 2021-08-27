package com.github.daniilandco.vehicle_sales_project.service.ad;

import com.github.daniilandco.vehicle_sales_project.controller.request.NewAdRequest;
import com.github.daniilandco.vehicle_sales_project.dto.model.ad.AdDto;
import com.github.daniilandco.vehicle_sales_project.exception.JwtAuthenticationException;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;

/**
 * Created by Daniel Bondarkov.
 */
public interface AdService {

    Iterable<AdDto> getAllAds();

    AdDto newAd(NewAdRequest request) throws JwtAuthenticationException;

    Iterable<AdDto> getUserAds() throws JwtAuthenticationException;

    void updateAd(Long id, NewAdRequest updatedAd) throws Exception;

    AdDto getAdById(Long id);

    void deleteUserAdById(Long id) throws Exception;

    AdDto getUserAdById(Long id);

    void uploadAdPhotos(Long id, MultipartFile[] images) throws Exception;

    URL getAdPhotoById(Long adId, int photoId) throws Exception;
}
