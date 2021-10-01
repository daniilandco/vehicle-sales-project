package com.github.daniilandco.vehicle_sales_project.repository.ad;

import com.github.daniilandco.vehicle_sales_project.model.ad.Ad;
import com.github.daniilandco.vehicle_sales_project.model.category.Category;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.Date;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface AdRepository extends CrudRepository<Ad, Long> {
    Iterable<Ad> findAllByCategoryAfter(Category category);

    Iterable<Ad> findAllByCategoryAfterAndPriceBetweenAndReleaseYearBetween
            (Category category, BigDecimal left, BigDecimal right, Date leftDate, Date rightDate);
}
