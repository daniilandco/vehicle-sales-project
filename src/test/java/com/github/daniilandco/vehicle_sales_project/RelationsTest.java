package com.github.daniilandco.vehicle_sales_project;

import com.github.daniilandco.vehicle_sales_project.model.ad.Ad;
import com.github.daniilandco.vehicle_sales_project.repository.ad.AdRepository;
import com.github.daniilandco.vehicle_sales_project.repository.user.UserRepository;
import org.junit.jupiter.api.Test;

public class RelationsTest {

    private final UserRepository userRepository;
    private final AdRepository adRepository;

    public RelationsTest(UserRepository userRepository, AdRepository adRepository) {
        this.userRepository = userRepository;
        this.adRepository = adRepository;
    }

    @Test
    public void deleteAd() {
        Ad ad = adRepository.findById(1L).get();
    }


}
