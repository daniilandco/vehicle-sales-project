package com.github.daniilandco.vehicle_sales_project.service;

import com.github.daniilandco.vehicle_sales_project.exception.category.CategoryException;
import com.github.daniilandco.vehicle_sales_project.model.category.Category;

public interface CategoryService {
    Category getCategory(String[] categoriesHierarchy) throws CategoryException;
}
