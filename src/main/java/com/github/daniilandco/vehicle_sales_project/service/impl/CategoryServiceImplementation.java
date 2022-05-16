package com.github.daniilandco.vehicle_sales_project.service.impl;

import com.github.daniilandco.vehicle_sales_project.exception.category.CategoryException;
import com.github.daniilandco.vehicle_sales_project.model.category.Category;
import com.github.daniilandco.vehicle_sales_project.repository.CategoryRepository;
import com.github.daniilandco.vehicle_sales_project.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryServiceImplementation implements CategoryService {
    private final CategoryRepository categoryRepository;

    public Category getCategory(String[] hierarchy) throws CategoryException {
        Category parentCategory = getCategoryByName(hierarchy[0]);

        for (int i = 1; i < hierarchy.length; ++i) {
            Category category = getCategoryByName(hierarchy[i]);
            if (parentCategory.getChildren().contains(category)) {
                parentCategory = category;
            } else {
                throw new CategoryException("no such subcategory");
            }
        }

        return getCategoryByName(hierarchy[hierarchy.length - 1]);
    }

    private Category getCategoryByName(String name) throws CategoryException {
        Optional<Category> category = categoryRepository.findByName(name);
        if (category.isPresent()) {
            return category.get();
        } else {
            throw new CategoryException("no such subcategory:" + name);
        }
    }
}
