package com.github.daniilandco.vehicle_sales_project.service.ad;

import com.github.daniilandco.vehicle_sales_project.controller.request.NewAdRequest;
import com.github.daniilandco.vehicle_sales_project.dto.mapper.AdMapper;
import com.github.daniilandco.vehicle_sales_project.dto.model.ad.AdDto;
import com.github.daniilandco.vehicle_sales_project.exception.JwtAuthenticationException;
import com.github.daniilandco.vehicle_sales_project.model.ad.Ad;
import com.github.daniilandco.vehicle_sales_project.model.photos.AdPhoto;
import com.github.daniilandco.vehicle_sales_project.model.user.User;
import com.github.daniilandco.vehicle_sales_project.repository.ad.AdRepository;
import com.github.daniilandco.vehicle_sales_project.repository.user.UserRepository;
import com.github.daniilandco.vehicle_sales_project.security.AuthContextHandler;
import com.github.daniilandco.vehicle_sales_project.service.gcs.GenerateV4GetObjectSignedUrl;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.Objects;
import java.util.stream.StreamSupport;

@Service
public class AdServiceImplementation implements AdService {
    private final UserRepository userRepository;
    private final AdRepository adRepository;
    private final Storage storage;
    private final AdMapper adMapper;
    private final GenerateV4GetObjectSignedUrl generateV4GetObjectSignedUrl;
    private final AuthContextHandler authContextHandler;

    @Value("${cloud.bucketname}")
    private String bucketName;
    @Value("${cloud.default.photo.extension}")
    private String defaultExtension;
    @Value("${cloud.subdirectory.ad}")
    private String adPhotosPath;

    public AdServiceImplementation(UserRepository userRepository, AdRepository adRepository, Storage storage, AdMapper adMapper, GenerateV4GetObjectSignedUrl generateV4GetObjectSignedUrl, AuthContextHandler authContextHandler) {
        this.userRepository = userRepository;
        this.adRepository = adRepository;
        this.storage = storage;
        this.adMapper = adMapper;
        this.generateV4GetObjectSignedUrl = generateV4GetObjectSignedUrl;
        this.authContextHandler = authContextHandler;
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
    public AdDto newAd(NewAdRequest request) throws JwtAuthenticationException {
        User userModel = authContextHandler.getLoggedInUser();
        Ad ad = new Ad(userModel, request.getTitle(), request.getDescription(), request.getMakeId(), request.getPrice(), request.getReleaseYear(), request.getStatus());
        userModel.getAds().add(ad);
        userRepository.save(userModel);
        return adMapper.toAdDto(ad);
    }

    @Override
    public Iterable<AdDto> getUserAds() throws JwtAuthenticationException {
        User user = authContextHandler.getLoggedInUser();
        Iterable<AdDto> adDtoSet = adMapper.toAdDtoSet(user.getAds());
        return adDtoSet;
    }

    @Override
    public void updateAd(Long id, NewAdRequest updatedAd) throws Exception {
        User user = authContextHandler.getLoggedInUser();
        Ad ad = user.getAds().stream().filter(adModel -> adModel.getId().equals(id)).findFirst().orElseThrow(() -> new Exception("ad doesn't exist or belong to logged in user"));
        if (updatedAd.getMakeId() != null) {
            ad.setMakeId(updatedAd.getMakeId());
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
    public void deleteUserAdById(Long id) throws Exception {
        User user = authContextHandler.getLoggedInUser();
        if (adRepository.existsById(id) && user.getAds().contains(adRepository.findById(id).get())) {
            user.getAds().remove(user.getAds().stream().filter(ad -> ad.getId().equals(id)).findFirst().orElseThrow(() -> new Exception("error")));
            userRepository.save(user);
            adRepository.deleteById(id);
        } else {
            throw new Exception("error");
        }
    }

    @Override
    public AdDto getUserAdById(Long id) {
        AdDto adDto = adMapper.toAdDto(Objects.requireNonNull(StreamSupport.stream(adRepository.findAll().spliterator(), false).
                filter(user -> user.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NullPointerException("ad doesn't exist or belong to logged in user"))));
        return adDto;
    }

    @Override
    public void uploadAdPhotos(Long id, MultipartFile[] images) throws Exception {
        User user = authContextHandler.getLoggedInUser();
        Ad ad = user.getAds().stream().filter(adModel -> adModel.getId().equals(id)).findFirst().orElseThrow(() -> new Exception("ad doesn't exist or belong to logged in user"));
        for (int counter = 0; counter < images.length; ++counter) {
            BlobId blobId = BlobId.of(bucketName, getUniqueAdPhotoPath(id, counter));
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
            byte[] bytes = images[counter].getBytes();
            storage.create(blobInfo, bytes);

            ad.getPhotos().add(new AdPhoto(ad, getUniqueAdPhotoPath(id, counter)));
            adRepository.save(ad);
        }
    }

    @Override
    public URL getAdPhotoById(Long adId, int photoId) throws Exception { // main ad photo name is '0.png'
        User user = authContextHandler.getLoggedInUser();
        if (user.getAds().contains(adRepository.findById(adId).orElseThrow(() -> new Exception("ad doesn't exist or belong to logged in user")))) {
            URL url = generateV4GetObjectSignedUrl.generate(bucketName, getUniqueAdPhotoPath(adId, photoId));
            return url;
        } else {
            throw new Exception("ad doesn't exist or belong to logged in user");
        }
    }

    private String getUniqueAdPhotoPath(Long id, int counter) {
        return adPhotosPath + id + "/" + counter + defaultExtension;
    }
}
