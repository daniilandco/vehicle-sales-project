package com.github.daniilandco.vehicle_sales_project.service.search;

import com.github.daniilandco.vehicle_sales_project.controller.request.SearchByParamsRequest;
import com.github.daniilandco.vehicle_sales_project.dto.mapper.AdMapper;
import com.github.daniilandco.vehicle_sales_project.dto.model.ad.AdDto;
import com.github.daniilandco.vehicle_sales_project.exception.CategoryException;
import com.github.daniilandco.vehicle_sales_project.model.ad.Ad;
import com.github.daniilandco.vehicle_sales_project.repository.ad.AdRepository;
import com.github.daniilandco.vehicle_sales_project.service.category.CategoryService;
import com.google.common.collect.Iterables;
import org.ahocorasick.trie.Trie;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class SearchEngineServiceImplementation implements SearchEngineService {

    private final AdRepository adRepository;
    private final CategoryService categoryService;
    private final AdMapper adMapper;

    public SearchEngineServiceImplementation(AdRepository adRepository, CategoryService categoryService, AdMapper adMapper) {
        this.adRepository = adRepository;
        this.categoryService = categoryService;
        this.adMapper = adMapper;
    }

    @Override
    public Iterable<AdDto> getAdsByQuery(String query) {
        String[] keywords = query.split(" ");
        Trie trie = Trie.builder().addKeywords(keywords).build();

        Set<Ad> result = new HashSet<>();

        adRepository.findAll().forEach(ad -> {
            if (!trie.parseText(ad.getTitle()).isEmpty()) {
                result.add(ad);
            }
        });

        return adMapper.toAdDtoSet(result);
    }

    @Override
    public Iterable<AdDto> getAdsByParams(SearchByParamsRequest request) throws CategoryException {
        Iterable<Ad> resultSet = (request.getCategoriesHierarchy() != null) ?
                adRepository.findAllByCategoryAfter(categoryService.getCategory(request.getCategoriesHierarchy())) : adRepository.findAll();

        resultSet = (request.getPriceRange() != null) ? Iterables.filter(resultSet, ad -> request.getPriceRange().contains(ad.getPrice())) : resultSet;
        resultSet = (request.getReleaseYearRange() != null) ? Iterables.filter(resultSet, ad -> request.getReleaseYearRange().contains(ad.getReleaseYear())) : resultSet;
        return adMapper.toAdDtoSet(resultSet);
    }
}
