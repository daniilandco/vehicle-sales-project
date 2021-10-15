package com.github.daniilandco.vehicle_sales_project.service.ad;

import com.github.daniilandco.vehicle_sales_project.controller.request.NewAdRequest;
import com.github.daniilandco.vehicle_sales_project.dto.mapper.AdMapper;
import com.github.daniilandco.vehicle_sales_project.dto.model.ad.AdDto;
import com.github.daniilandco.vehicle_sales_project.exception.*;
import com.github.daniilandco.vehicle_sales_project.model.ad.Ad;
import com.github.daniilandco.vehicle_sales_project.model.photos.AdPhoto;
import com.github.daniilandco.vehicle_sales_project.model.user.User;
import com.github.daniilandco.vehicle_sales_project.repository.ad.AdElasticSearchRepository;
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
    private final AdElasticSearchRepository adElasticSearchRepository;
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

    public AdServiceImplementation(UserRepository userRepository, AdRepository adRepository, Storage storage, AdMapper adMapper, GoogleStorageSignedUrlGenerator googleStorageSignedUrlGenerator, AuthContextHandler authContextHandler, CategoryService categoryService, ImageService imageService, AdElasticSearchRepository adElasticSearchRepository) {
        this.userRepository = userRepository;
        this.adRepository = adRepository;
        this.storage = storage;
        this.adMapper = adMapper;
        this.googleStorageSignedUrlGenerator = googleStorageSignedUrlGenerator;
        this.authContextHandler = authContextHandler;
        this.categoryService = categoryService;
        this.imageService = imageService;
        this.adElasticSearchRepository = adElasticSearchRepository;
    }

    @Override
    public Iterable<AdDto> getAllAds() {
        return adMapper.toAdDtoSet(adRepository.findAll());
    }

    @Override
    public AdDto getAdById(Long id) throws AdNotFoundException {
        return adMapper.toAdDto(getAdModelById(id));
    }

    @Override
    public AdDto newAd(NewAdRequest request) throws UserIsNotLoggedInException, CategoryException {
        User userModel = authContextHandler.getLoggedInUser();
        Ad ad = new Ad(userModel, request.getTitle(), request.getDescription(), categoryService.getCategory(request.getCategoriesHierarchy()), request.getPrice(), request.getReleaseYear(), request.getStatus());
        userModel.getAds().add(ad);

        userRepository.save(userModel); // updates list of ads of logged-in user

        var updatedAdsList = userModel.getAds();
        Ad adWithGeneratedId = updatedAdsList.get(updatedAdsList.size() - 1);

        adElasticSearchRepository.save(adWithGeneratedId); // saves new ad in elastic search index

        return adMapper.toAdDto(adWithGeneratedId);
    }

    @Override
    public Iterable<AdDto> getUserAds() throws UserIsNotLoggedInException {
        User user = authContextHandler.getLoggedInUser();
        return adMapper.toAdDtoSet(user.getAds());
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

        userRepository.save(user); // update ad of logged-in user
        adElasticSearchRepository.save(ad); // update ad in elastic search index
    }

    @Override
    public void deleteUserAdById(Long id) throws AdNotFoundException, UserIsNotLoggedInException, AdDoesNotBelongToLoggedInUserException {
        User user = authContextHandler.getLoggedInUser();
        if (adRepository.existsById(id) && user.getAds().contains(getAdModelById(id))) {
            user.getAds().remove(user.getAds().stream().filter(ad -> ad.getId().equals(id)).findFirst().orElseThrow(AdDoesNotBelongToLoggedInUserException::new));
            userRepository.save(user);

            adRepository.deleteById(id);
            adElasticSearchRepository.deleteById(id);
        } else {
            throw new AdNotFoundException();
        }
    }

    @Override
    public Ad getAdModelById(Long id) throws AdNotFoundException {
        Optional<Ad> ad = adRepository.findById(id);
        if (ad.isPresent()) {
            return ad.get();
        } else {
            throw new AdNotFoundException("no such ad with id:" + id);
        }
    }

    @Override
    public AdDto getUserAdById(Long id) throws AdNotFoundException {
        return adMapper.toAdDto(Objects.requireNonNull(StreamSupport.stream(adRepository.findAll().spliterator(), false).
                filter(user -> user.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new AdNotFoundException("ad doesn't exist or belong to logged in user"))));
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
        if (user.getAds().contains(adRepository.findById(adId).orElseThrow(AdDoesNotBelongToLoggedInUserException::new))) {
            return googleStorageSignedUrlGenerator.generate(bucketName, getUniqueAdPhotoPath(adId, photoId));
        } else {
            throw new AdNotFoundException();
        }
    }

    private String getUniqueAdPhotoPath(Long id, int counter) {
        return adPhotosPath + id + "/" + counter + "." + defaultFormat;
    }
}
