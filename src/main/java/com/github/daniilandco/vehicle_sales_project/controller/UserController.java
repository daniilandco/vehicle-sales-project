package com.github.daniilandco.vehicle_sales_project.controller;

import com.github.daniilandco.vehicle_sales_project.controller.request.NewAdRequest;
import com.github.daniilandco.vehicle_sales_project.model.ad.Ad;
import com.github.daniilandco.vehicle_sales_project.model.user.User;
import com.github.daniilandco.vehicle_sales_project.repository.user.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/ads")
    public Iterable<Ad> getUserAds() {
        Optional<User> user = userRepository.findByEmail("user");
        return user.get().getAds();
    }

    @PostMapping("/create_new_ad")
    public String addAd(@RequestBody NewAdRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        if (email != null) {
            Optional<User> userFromDb = userRepository.findByEmail(email);
            if (userFromDb.isPresent()) {
                User user = userFromDb.get();
                Ad ad = new Ad(user, request.getTitle(), request.getDescription(), request.getMakeId(), request.getPrice(), request.getRelease_year(), request.getStatus());
                user.getAds().add(ad);
                userRepository.save(user);
                return "success";
            } else {
                return "failed";
            }
        } else return "failed";
    }

}
