package com.github.daniilandco.vehicle_sales_project.controller.request;

import lombok.Data;
import org.elasticsearch.search.sort.SortOrder;

@Data
public class SearchByQueryRequest extends PagePaginationRequest {
    private String[] fields;
    private String terms;
    private String sortBy;
    private SortOrder order;
}
