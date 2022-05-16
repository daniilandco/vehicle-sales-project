package com.github.daniilandco.vehicle_sales_project.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.elasticsearch.search.sort.SortOrder;

@Data
public class SearchByQueryRequestDTO extends PagePaginationRequestDTO {
    private String[] fields;
    private String terms;
    @JsonProperty("sort_by")
    private String sortBy;
    private SortOrder order;
}
