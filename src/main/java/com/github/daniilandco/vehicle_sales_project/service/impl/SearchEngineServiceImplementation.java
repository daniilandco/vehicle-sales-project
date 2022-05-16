package com.github.daniilandco.vehicle_sales_project.service.impl;

import com.github.daniilandco.vehicle_sales_project.dto.model.AdDTO;
import com.github.daniilandco.vehicle_sales_project.dto.request.SearchByParamsRequestDTO;
import com.github.daniilandco.vehicle_sales_project.dto.request.SearchByQueryRequestDTO;
import com.github.daniilandco.vehicle_sales_project.exception.ad.AdNotFoundException;
import com.github.daniilandco.vehicle_sales_project.exception.category.CategoryException;
import com.github.daniilandco.vehicle_sales_project.model.ad.Indices;
import com.github.daniilandco.vehicle_sales_project.model.ad.Status;
import com.github.daniilandco.vehicle_sales_project.model.category.Category;
import com.github.daniilandco.vehicle_sales_project.service.AdService;
import com.github.daniilandco.vehicle_sales_project.service.CategoryService;
import com.github.daniilandco.vehicle_sales_project.service.SearchEngineService;
import lombok.AllArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SearchEngineServiceImplementation implements SearchEngineService {
    private final RestHighLevelClient client;
    private final AdService adService;
    private final CategoryService categoryService;

    public static SearchRequest buildSearchRequest(final SearchByQueryRequestDTO request) {

        final int page = request.getPage();
        final int size = request.getSize();
        final int from = page * size;

        SearchSourceBuilder builder = new SearchSourceBuilder()
                .from(from)
                .size(size)
                .postFilter(getQueryBuilder(request));

        if (request.getSortBy() != null) {
            builder = builder.sort(
                    request.getSortBy(),
                    request.getOrder() != null ? request.getOrder() : SortOrder.ASC
            );
        }

        final SearchRequest searchRequest = new SearchRequest(Indices.AD_INDEX);
        searchRequest.source(builder);

        return searchRequest;
    }

    public static SearchRequest buildSearchRequest(final SearchByParamsRequestDTO request) {
        final int page = request.getPage();
        final int size = request.getSize();
        final int from = page * size;

        final SearchSourceBuilder builder = new SearchSourceBuilder()
                .from(from)
                .size(size)
                .postFilter(getQueryBuilder(request));

        final SearchRequest searchRequest = new SearchRequest(Indices.AD_INDEX);
        searchRequest.source(builder);

        return searchRequest;
    }


    public static QueryBuilder getQueryBuilder(SearchByQueryRequestDTO request) {
        MultiMatchQueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(request.getTerms())
                .type(MultiMatchQueryBuilder.Type.CROSS_FIELDS)
                .operator(Operator.OR);

        for (String field : request.getFields()) {
            queryBuilder.field(field);
        }

        return QueryBuilders.boolQuery()
                .must(queryBuilder)
                .must(QueryBuilders.matchQuery("status", Status.ACTIVE.toString()));
    }

    public static QueryBuilder getQueryBuilder(final SearchByParamsRequestDTO request) {
        return QueryBuilders.boolQuery()
                .must(QueryBuilders.rangeQuery("price").from(request.getPriceFrom()).to(request.getPriceTo()))
                .must(QueryBuilders.rangeQuery("releaseYear").from(request.getReleaseYearFrom()).to(request.getReleaseYearTo()))
                .must(QueryBuilders.matchQuery("status", Status.ACTIVE.toString()));
    }

    @Override
    public Iterable<AdDTO> search(SearchByQueryRequestDTO request) throws IOException, AdNotFoundException {
        final SearchRequest searchRequest = buildSearchRequest(request);
        return getSearchResultsByRequest(searchRequest);
    }

    @Override
    public List<AdDTO> search(SearchByParamsRequestDTO request) throws AdNotFoundException, IOException, CategoryException {
        final SearchRequest searchRequest = buildSearchRequest(request);

        Category category = categoryService.getCategory(request.getCategoriesHierarchy());

        return getSearchResultsByRequest(searchRequest)
                .stream().filter(ad -> ad.getCategory().isRelative(category))
                .collect(Collectors.toList());
    }

    private List<AdDTO> getSearchResultsByRequest(SearchRequest searchRequest) throws IOException, AdNotFoundException {
        final SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

        final SearchHit[] searchHits = response.getHits().getHits();
        final List<AdDTO> ads = new ArrayList<>(searchHits.length);
        for (var hit : searchHits) {
            ads.add(adService.getAdById(Long.parseLong(hit.getId())));
        }
        return ads;
    }
}
