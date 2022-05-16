package com.github.daniilandco.vehicle_sales_project.service;

import com.github.daniilandco.vehicle_sales_project.dto.model.AdDTO;
import com.github.daniilandco.vehicle_sales_project.dto.request.SearchByParamsRequestDTO;
import com.github.daniilandco.vehicle_sales_project.dto.request.SearchByQueryRequestDTO;
import com.github.daniilandco.vehicle_sales_project.exception.ad.AdNotFoundException;
import com.github.daniilandco.vehicle_sales_project.exception.category.CategoryException;

import java.io.IOException;

public interface SearchEngineService {
    Iterable<AdDTO> search(SearchByQueryRequestDTO request) throws IOException, AdNotFoundException;

    Iterable<AdDTO> search(SearchByParamsRequestDTO request) throws CategoryException, AdNotFoundException, IOException;
}
