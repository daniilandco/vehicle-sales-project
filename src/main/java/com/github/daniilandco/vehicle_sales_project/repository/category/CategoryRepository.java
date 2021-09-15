package com.github.daniilandco.vehicle_sales_project.repository.category;

import com.github.daniilandco.vehicle_sales_project.model.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
}
