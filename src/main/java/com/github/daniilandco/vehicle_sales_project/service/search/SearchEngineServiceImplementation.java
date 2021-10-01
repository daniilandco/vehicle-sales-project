package com.github.daniilandco.vehicle_sales_project.service.search;

import com.github.daniilandco.vehicle_sales_project.controller.request.SearchByParamsRequest;
import com.github.daniilandco.vehicle_sales_project.dto.mapper.AdMapper;
import com.github.daniilandco.vehicle_sales_project.dto.model.ad.AdDto;
import com.github.daniilandco.vehicle_sales_project.exception.CategoryException;
import com.github.daniilandco.vehicle_sales_project.repository.ad.AdHibernateSearchRepository;
import org.springframework.stereotype.Service;

@Service
public class SearchEngineServiceImplementation implements SearchEngineService {

    private final AdMapper adMapper;
    private final AdHibernateSearchRepository adHibernateSearchRepository;

    public SearchEngineServiceImplementation(AdMapper adMapper, AdHibernateSearchRepository adHibernateSearchRepository) {
        this.adMapper = adMapper;
        this.adHibernateSearchRepository = adHibernateSearchRepository;
    }

    @Override
    public Iterable<AdDto> searchQuery(String queryRequest) {
        return adMapper.toAdDtoSet(adHibernateSearchRepository.search(queryRequest));
    }

    @Override
    public Iterable<AdDto> searchParams(SearchByParamsRequest request) throws CategoryException {
        return adMapper.toAdDtoSet(adHibernateSearchRepository.search(request));
    }
}
