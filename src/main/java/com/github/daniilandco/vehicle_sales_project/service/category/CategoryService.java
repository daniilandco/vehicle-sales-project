package com.github.daniilandco.vehicle_sales_project.service.category;

import com.github.daniilandco.vehicle_sales_project.exception.CategoryException;
import com.github.daniilandco.vehicle_sales_project.model.category.Category;

public interface CategoryService {
    Category getCategory(String[] categoriesHierarchy) throws CategoryException;
}
