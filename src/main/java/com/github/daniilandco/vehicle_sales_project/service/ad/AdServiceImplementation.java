package com.github.daniilandco.vehicle_sales_project.service.ad;

import com.github.daniilandco.vehicle_sales_project.controller.request.NewAdRequest;
import com.github.daniilandco.vehicle_sales_project.controller.response.RestApiResponse;
import com.github.daniilandco.vehicle_sales_project.dto.mapper.AdMapper;
import com.github.daniilandco.vehicle_sales_project.dto.model.ad.AdDto;
import com.github.daniilandco.vehicle_sales_project.model.ad.Ad;
import com.github.daniilandco.vehicle_sales_project.model.user.User;
import com.github.daniilandco.vehicle_sales_project.repository.ad.AdRepository;
import com.github.daniilandco.vehicle_sales_project.repository.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class AdServiceImplementation implements AdService {
    private final UserRepository userRepository;
    private final AdRepository adRepository;

    public AdServiceImplementation(UserRepository userRepository, AdRepository adRepository) {
        this.userRepository = userRepository;
        this.adRepository = adRepository;
    }

    @Override
    public ResponseEntity<?> getAllAds() {
        return ResponseEntity
                .ok(new RestApiResponse(HttpStatus.OK.value(),
                        "", AdMapper.toAdDtoSet(adRepository.findAll())));
    }

    @Override
    public ResponseEntity<?> getAdById(Long id) {
        AdDto adDto = AdMapper.toAdDto(Objects.requireNonNull(StreamSupport.stream(adRepository.findAll().spliterator(), false).
                filter(ad -> ad.getId().equals(id))
                .findFirst()
                .orElse(null)));
        return ResponseEntity
                .ok(new RestApiResponse(HttpStatus.OK.value(),
                        "", adDto));
    }

    @Override
    public ResponseEntity<?> addAd(NewAdRequest request) {
        Optional<User> userFromDb = getLoggedInUser();
        if (userFromDb.isPresent()) {
            User user = userFromDb.get();
            Ad ad = new Ad(user, request.getTitle(), request.getDescription(), request.getMakeId(), request.getPrice(), request.getReleaseYear(), request.getStatus());
            user.getAds().add(ad);
            userRepository.save(user);
            return ResponseEntity
                    .ok(new RestApiResponse(HttpStatus.OK.value(), "New ad is created", AdMapper.toAdDto(ad)));
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new RestApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "error"));
        }

    }

    @Override
    public ResponseEntity<?> getUserAds() {
        Optional<User> user = getLoggedInUser();
        Iterable<AdDto> adDtoSet = AdMapper.toAdDtoSet(user.get().getAds()); // handle exception
        return ResponseEntity.ok(new RestApiResponse(HttpStatus.OK.value(), "Ok", adDtoSet));
    }

    /**
     * Updates the agency with given Bus information
     */
    @Override
    public ResponseEntity<?> updateAd(Long id, NewAdRequest updatedAd) {
        Optional<Ad> ad = adRepository.findById(id);
        if (ad.isPresent()) {
            Ad adModel = ad.get();
            if (updatedAd.getMakeId() != null) adModel.setMakeId(updatedAd.getMakeId());
            if (updatedAd.getTitle() != null) adModel.setTitle(updatedAd.getTitle());
            if (updatedAd.getDescription() != null) adModel.setDescription(updatedAd.getDescription());
            if (updatedAd.getPrice() != null) adModel.setPrice(updatedAd.getPrice());
            if (updatedAd.getReleaseYear() != null) adModel.setReleaseYear(updatedAd.getReleaseYear());

            return ResponseEntity.ok(
                    new RestApiResponse(HttpStatus.OK.value(), "Ad has been updated",
                            AdMapper.toAdDto(adRepository.save(adModel))));
        } else return ResponseEntity
                .badRequest()
                .body(new RestApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "error"));
    }

    @Override
    public ResponseEntity<?> deleteUserAdById(Long id) {
        if (adRepository.existsById(id)) {
            adRepository.deleteById(id);
            return ResponseEntity.ok(
                    new RestApiResponse(HttpStatus.OK.value(), "Ad is deleted"));
        } else return ResponseEntity
                .badRequest()
                .body(new RestApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "error"));
    }

    @Override
    public ResponseEntity<?> getUserAdById(Long id) {
        try {
            AdDto adDto = AdMapper.toAdDto(Objects.requireNonNull(StreamSupport.stream(adRepository.findAll().spliterator(), false).
                    filter(user -> user.getId().equals(id))
                    .findFirst()
                    .orElse(null)));
            return ResponseEntity.ok(new RestApiResponse(HttpStatus.OK.value(), "Ad exists", adDto));
        } catch (NullPointerException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new RestApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Ad doesn't exist or belong to logged in user"));
        }
    }

    private Optional<User> getLoggedInUser() { //repeat!!
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return userRepository.findByEmail(email);
    }
}
