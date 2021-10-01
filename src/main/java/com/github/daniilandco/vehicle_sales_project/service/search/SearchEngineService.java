package com.github.daniilandco.vehicle_sales_project.service.search;

import com.github.daniilandco.vehicle_sales_project.controller.request.SearchByParamsRequest;
import com.github.daniilandco.vehicle_sales_project.dto.model.ad.AdDto;
import com.github.daniilandco.vehicle_sales_project.exception.CategoryException;
import org.springframework.stereotype.Service;

@Service
public interface SearchEngineService {
    Iterable<AdDto> searchQuery(String queryRequest);

    Iterable<AdDto> searchParams(SearchByParamsRequest request) throws CategoryException;
}
