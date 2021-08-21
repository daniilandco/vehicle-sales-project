package com.github.daniilandco.vehicle_sales_project.service.ad;

import com.github.daniilandco.vehicle_sales_project.controller.request.NewAdRequest;
import com.github.daniilandco.vehicle_sales_project.controller.response.RestApiResponse;
import com.github.daniilandco.vehicle_sales_project.dto.mapper.AdMapper;
import com.github.daniilandco.vehicle_sales_project.dto.model.ad.AdDto;
import com.github.daniilandco.vehicle_sales_project.model.ad.Ad;
import com.github.daniilandco.vehicle_sales_project.model.user.User;
import com.github.daniilandco.vehicle_sales_project.repository.ad.AdRepository;
import com.github.daniilandco.vehicle_sales_project.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Component
public class AdServiceImplementation implements AdService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdRepository adRepository;

    @Override
    public ResponseEntity<?> addAd(NewAdRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Optional<User> userFromDb = userRepository.findByEmail(email);
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
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Optional<User> user = userRepository.findByEmail(email);
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
    public ResponseEntity<?> getAdById(Long id) {
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
}
