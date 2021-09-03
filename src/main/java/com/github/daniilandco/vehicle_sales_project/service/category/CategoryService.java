package com.github.daniilandco.vehicle_sales_project.service.category;

import com.github.daniilandco.vehicle_sales_project.controller.request.CategoryRequest;
import com.github.daniilandco.vehicle_sales_project.model.category.Category;

public interface CategoryService {
    Category getCategory(CategoryRequest request) throws Exception;
}
