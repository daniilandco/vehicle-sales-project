package com.github.daniilandco.vehicle_sales_project.controller.request;

import lombok.Data;

@Data
public class PagePaginationRequest {
    private static final int DEFAULT_SIZE = 50;

    private int page;
    private int size = DEFAULT_SIZE;
}
