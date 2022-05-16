package com.github.daniilandco.vehicle_sales_project.service.impl;

import com.github.daniilandco.vehicle_sales_project.dto.model.AdDTO;
import com.github.daniilandco.vehicle_sales_project.dto.request.AdRequestDTO;
import com.github.daniilandco.vehicle_sales_project.exception.ad.AdDoesNotBelongToLoggedInUserException;
import com.github.daniilandco.vehicle_sales_project.exception.ad.AdNotFoundException;
import com.github.daniilandco.vehicle_sales_project.exception.auth.UserIsNotLoggedInException;
import com.github.daniilandco.vehicle_sales_project.exception.category.CategoryException;
import com.github.daniilandco.vehicle_sales_project.exception.image.AdPhotoNotFoundException;
import com.github.daniilandco.vehicle_sales_project.exception.image.InvalidImageSizeException;
import com.github.daniilandco.vehicle_sales_project.mapper.AdMapper;
import com.github.daniilandco.vehicle_sales_project.model.ad.Ad;
import com.github.daniilandco.vehicle_sales_project.model.photos.AdPhoto;
import com.github.daniilandco.vehicle_sales_project.model.user.User;
import com.github.daniilandco.vehicle_sales_project.repository.AdElasticSearchRepository;
import com.github.daniilandco.vehicle_sales_project.repository.AdPhotoRepository;
import com.github.daniilandco.vehicle_sales_project.repository.AdRepository;
import com.github.daniilandco.vehicle_sales_project.repository.UserRepository;
import com.github.daniilandco.vehicle_sales_project.security.AuthContextHandler;
import com.github.daniilandco.vehicle_sales_project.service.AdService;
import com.github.daniilandco.vehicle_sales_project.service.CategoryService;
import com.github.daniilandco.vehicle_sales_project.service.ImageService;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class AdServiceImplementation implements AdService {
    private final UserRepository userRepository;
    private final AdRepository adRepository;
    private final AdPhotoRepository adPhotoRepository;
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

    public AdServiceImplementation(UserRepository userRepository, AdRepository adRepository, AdPhotoRepository adPhotoRepository, AdElasticSearchRepository adElasticSearchRepository, Storage storage, AdMapper adMapper, GoogleStorageSignedUrlGenerator googleStorageSignedUrlGenerator, AuthContextHandler authContextHandler, CategoryService categoryService, ImageService imageService) {
        this.userRepository = userRepository;
        this.adRepository = adRepository;
        this.adPhotoRepository = adPhotoRepository;
        this.adElasticSearchRepository = adElasticSearchRepository;
        this.storage = storage;
        this.adMapper = adMapper;
        this.googleStorageSignedUrlGenerator = googleStorageSignedUrlGenerator;
        this.authContextHandler = authContextHandler;
        this.categoryService = categoryService;
        this.imageService = imageService;
    }

    @Override
    public Iterable<AdDTO> getAllAds() {
        return adMapper.toAdDTOList((List<Ad>) adRepository.findAll());
    }

    @Override
    public AdDTO getAdById(Long id) throws AdNotFoundException {
        return adMapper.toAdDto(getAdModelById(id));
    }

    @Override
    public AdDTO newAd(AdRequestDTO request) throws UserIsNotLoggedInException, CategoryException {
        User userModel = authContextHandler.getLoggedInUser();
        Ad ad = new Ad(userModel, request.title(), request.description(),
                categoryService.getCategory(request.categoriesHierarchy()),
                request.price(), request.releaseYear(), request.status());
        userModel.getAds().add(ad);

        userRepository.save(userModel); // updates list of ads of logged-in user

        var updatedAdsList = userModel.getAds();
        Ad adWithGeneratedId = updatedAdsList.get(updatedAdsList.size() - 1);

        adElasticSearchRepository.save(adWithGeneratedId); // saves new ad in elastic search index

        return adMapper.toAdDto(adWithGeneratedId);
    }

    @Override
    public Iterable<AdDTO> getUserAds() throws UserIsNotLoggedInException {
        User user = authContextHandler.getLoggedInUser();
        return adMapper.toAdDTOList(user.getAds());
    }

    @Override
    public AdDTO updateAd(Long id, AdRequestDTO updatedAd) throws UserIsNotLoggedInException, CategoryException, AdNotFoundException {
        User user = authContextHandler.getLoggedInUser();
        Ad ad = user.getAds().stream().filter(adModel -> adModel.getId().equals(id)).findFirst().orElseThrow(() -> new AdNotFoundException("ad doesn't exist or belong to logged in user"));
        if (updatedAd.categoriesHierarchy() != null) {
            ad.setCategory(categoryService.getCategory(updatedAd.categoriesHierarchy()));
        }
        if (updatedAd.title() != null) {
            ad.setTitle(updatedAd.title());
        }
        if (updatedAd.description() != null) {
            ad.setDescription(updatedAd.description());
        }
        if (updatedAd.price() != null) {
            ad.setPrice(updatedAd.price());
        }
        if (updatedAd.releaseYear() != null) {
            ad.setReleaseYear(updatedAd.releaseYear());
        }

        userRepository.save(user); // update ad of logged-in user
        adElasticSearchRepository.save(ad); // update ad in elastic search index

        return adMapper.toAdDto(ad);
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
            throw new AdNotFoundException("ad with id=%d not found".formatted(id));
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
    public AdDTO getUserAdById(Long id) throws AdNotFoundException {
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
    public URL getAdPhotoById(Long adId, int photoId) throws AdPhotoNotFoundException { // main ad photo name is '0.png'
        if (adPhotoRepository.existsByAd(adId)) {
            return googleStorageSignedUrlGenerator.generate(bucketName, getUniqueAdPhotoPath(adId, photoId));
        } else {
            throw new AdPhotoNotFoundException("no photos belong to an ad");
        }
    }

    private String getUniqueAdPhotoPath(Long id, int counter) {
        return "%s%d/%d.%s".formatted(adPhotosPath, id, counter, defaultFormat);
    }
}
