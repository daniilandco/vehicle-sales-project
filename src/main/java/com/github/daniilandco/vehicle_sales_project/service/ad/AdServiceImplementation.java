package com.github.daniilandco.vehicle_sales_project.service.ad;

import com.github.daniilandco.vehicle_sales_project.controller.request.NewAdRequest;
import com.github.daniilandco.vehicle_sales_project.dto.mapper.AdMapper;
import com.github.daniilandco.vehicle_sales_project.dto.model.ad.AdDto;
import com.github.daniilandco.vehicle_sales_project.exception.*;
import com.github.daniilandco.vehicle_sales_project.model.ad.Ad;
import com.github.daniilandco.vehicle_sales_project.model.photos.AdPhoto;
import com.github.daniilandco.vehicle_sales_project.model.user.User;
import com.github.daniilandco.vehicle_sales_project.repository.ad.AdRepository;
import com.github.daniilandco.vehicle_sales_project.repository.user.UserRepository;
import com.github.daniilandco.vehicle_sales_project.security.AuthContextHandler;
import com.github.daniilandco.vehicle_sales_project.service.category.CategoryService;
import com.github.daniilandco.vehicle_sales_project.service.gcs.GoogleStorageSignedUrlGenerator;
import com.github.daniilandco.vehicle_sales_project.service.image.ImageService;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class AdServiceImplementation implements AdService {
    private final UserRepository userRepository;
    private final AdRepository adRepository;
    private final Storage storage;
    private final AdMapper adMapper;
    private final GoogleStorageSignedUrlGenerator googleStorageSignedUrlGenerator;
    private final AuthContextHandler authContextHandler;
    private final CategoryService categoryService;
    private final ImageService imageService;

    @Value("${cloud.bucketname}")
    private String bucketName;
    @Value("${cloud.default.photo.format}")
    private String defaultFormat;
    @Value("${cloud.subdirectory.ad}")
    private String adPhotosPath;

    public AdServiceImplementation(UserRepository userRepository, AdRepository adRepository, Storage storage, AdMapper adMapper, GoogleStorageSignedUrlGenerator googleStorageSignedUrlGenerator, AuthContextHandler authContextHandler, CategoryService categoryService, ImageService imageService) {
        this.userRepository = userRepository;
        this.adRepository = adRepository;
        this.storage = storage;
        this.adMapper = adMapper;
        this.googleStorageSignedUrlGenerator = googleStorageSignedUrlGenerator;
        this.authContextHandler = authContextHandler;
        this.categoryService = categoryService;
        this.imageService = imageService;
    }

    @Override
    public Iterable<AdDto> getAllAds() {
        return adMapper.toAdDtoSet(adRepository.findAll());
    }

    @Override
    public AdDto getAdById(Long id) {
        AdDto adDto = adMapper.toAdDto(Objects.requireNonNull(StreamSupport.stream(adRepository.findAll().spliterator(), false).
                filter(ad -> ad.getId().equals(id))
                .findFirst()
                .orElse(null)));
        return adDto;
    }

    @Override
    public AdDto newAd(NewAdRequest request) throws UserIsNotLoggedInException, CategoryException {
        User userModel = authContextHandler.getLoggedInUser();
        Ad ad = new Ad(userModel, request.getTitle(), request.getDescription(), categoryService.getCategory(request.getCategoriesHierarchy()), request.getPrice(), request.getReleaseYear(), request.getStatus());
        userModel.getAds().add(ad);
        userRepository.save(userModel);
        return adMapper.toAdDto(ad);
    }

    @Override
    public Iterable<AdDto> getUserAds() throws UserIsNotLoggedInException {
        User user = authContextHandler.getLoggedInUser();
        Iterable<AdDto> adDtoSet = adMapper.toAdDtoSet(user.getAds());
        return adDtoSet;
    }

    @Override
    public void updateAd(Long id, NewAdRequest updatedAd) throws UserIsNotLoggedInException, CategoryException, AdNotFoundException {
        User user = authContextHandler.getLoggedInUser();
        Ad ad = user.getAds().stream().filter(adModel -> adModel.getId().equals(id)).findFirst().orElseThrow(() -> new AdNotFoundException("ad doesn't exist or belong to logged in user"));
        if (updatedAd.getCategoriesHierarchy() != null) {
            ad.setCategory(categoryService.getCategory(updatedAd.getCategoriesHierarchy()));
        }
        if (updatedAd.getTitle() != null) {
            ad.setTitle(updatedAd.getTitle());
        }
        if (updatedAd.getDescription() != null) {
            ad.setDescription(updatedAd.getDescription());
        }
        if (updatedAd.getPrice() != null) {
            ad.setPrice(updatedAd.getPrice());
        }
        if (updatedAd.getReleaseYear() != null) {
            ad.setReleaseYear(updatedAd.getReleaseYear());
        }
    }

    @Override
    public void deleteUserAdById(Long id) throws AdNotFoundException, UserIsNotLoggedInException, AdDoesNotBelongToLoggedInUserException {
        User user = authContextHandler.getLoggedInUser();
        if (adRepository.existsById(id) && user.getAds().contains(getAdModelById(id))) {
            user.getAds().remove(user.getAds().stream().filter(ad -> ad.getId().equals(id)).findFirst().orElseThrow(() -> new AdDoesNotBelongToLoggedInUserException()));
            userRepository.save(user);
            adRepository.deleteById(id);
        } else {
            throw new AdNotFoundException();
        }
    }

    private Ad getAdModelById(Long id) throws AdNotFoundException {
        Optional<Ad> ad = adRepository.findById(id);
        if (ad.isPresent()) {
            return ad.get();
        } else {
            throw new AdNotFoundException("no such ad with id:" + id);
        }
    }

    @Override
    public AdDto getUserAdById(Long id) throws AdNotFoundException {
        AdDto adDto = adMapper.toAdDto(Objects.requireNonNull(StreamSupport.stream(adRepository.findAll().spliterator(), false).
                filter(user -> user.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new AdNotFoundException("ad doesn't exist or belong to logged in user"))));
        return adDto;
    }

    @Override
    public void uploadAdPhotos(Long id, byte[][] images) throws UserIsNotLoggedInException, AdNotFoundException, IOException, InvalidImageSizeException {
        User user = authContextHandler.getLoggedInUser();
        Ad ad = user.getAds().stream().filter(adModel -> adModel.getId().equals(id)).findFirst().orElseThrow(() -> new AdNotFoundException("ad doesn't exist or belong to logged in user"));
        for (byte[] bytes : images) {
            for (int counter = 0; counter < images.length; ++counter) {
                BlobId blobId = BlobId.of(bucketName, getUniqueAdPhotoPath(id, counter));
                BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
                storage.create(blobInfo, imageService.scaleImage(bytes));

                ad.getPhotos().add(new AdPhoto(ad, getUniqueAdPhotoPath(id, counter)));
                adRepository.save(ad);
            }
        }
    }

    @Override
    public URL getAdPhotoById(Long adId, int photoId) throws AdDoesNotBelongToLoggedInUserException, UserIsNotLoggedInException, AdNotFoundException { // main ad photo name is '0.png'
        User user = authContextHandler.getLoggedInUser();
        if (user.getAds().contains(adRepository.findById(adId).orElseThrow(() -> new AdDoesNotBelongToLoggedInUserException()))) {
            URL url = googleStorageSignedUrlGenerator.generate(bucketName, getUniqueAdPhotoPath(adId, photoId));
            return url;
        } else {
            throw new AdNotFoundException();
        }
    }

    private String getUniqueAdPhotoPath(Long id, int counter) {
        return adPhotosPath + id + "/" + counter + "." + defaultFormat;
    }
}
