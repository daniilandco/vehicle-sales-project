package com.github.daniilandco.vehicle_sales_project.service.category;

import com.github.daniilandco.vehicle_sales_project.exception.CategoryException;
import com.github.daniilandco.vehicle_sales_project.model.category.Category;
import com.github.daniilandco.vehicle_sales_project.repository.category.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryServiceImplementation implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImplementation(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

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
