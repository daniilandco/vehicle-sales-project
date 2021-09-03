package com.github.daniilandco.vehicle_sales_project.controller.request;

import lombok.Data;

@Data
public class CategoryRequest {
    String[] hierarchy;

    public CategoryRequest() {
    }

    public CategoryRequest(String[] hierarchy) {
        this.hierarchy = hierarchy;
    }
}
