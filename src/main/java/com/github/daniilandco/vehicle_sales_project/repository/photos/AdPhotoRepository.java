package com.github.daniilandco.vehicle_sales_project.repository.photos;

import com.github.daniilandco.vehicle_sales_project.model.photos.AdPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdPhotoRepository extends JpaRepository<AdPhoto, Long> {
    @Query("select (count(a) > 0) from AdPhoto a where a.ad.id = ?1")
    boolean existsByAd(Long id);
}
