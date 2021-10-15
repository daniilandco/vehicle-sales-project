package com.github.daniilandco.vehicle_sales_project.service.search;

import com.github.daniilandco.vehicle_sales_project.controller.request.SearchByParamsRequest;
import com.github.daniilandco.vehicle_sales_project.controller.request.SearchByQueryRequest;
import com.github.daniilandco.vehicle_sales_project.dto.model.ad.AdDto;
import com.github.daniilandco.vehicle_sales_project.exception.ad.AdNotFoundException;
import com.github.daniilandco.vehicle_sales_project.exception.category.CategoryException;

import java.io.IOException;

public interface SearchEngineService {
    Iterable<AdDto> search(SearchByQueryRequest request) throws IOException, AdNotFoundException;

    Iterable<AdDto> search(SearchByParamsRequest request) throws CategoryException, AdNotFoundException, IOException;
}
