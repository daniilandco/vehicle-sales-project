package com.github.daniilandco.vehicle_sales_project.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class SearchByParamsRequestDTO extends PagePaginationRequestDTO {
    @JsonProperty("price_from")
    private BigDecimal priceFrom;
    @JsonProperty("price_to")
    private BigDecimal priceTo;
    @JsonProperty("categories_hierarchy")
    private String[] categoriesHierarchy;
    @JsonProperty("release_year_from")
    private Date releaseYearFrom;
    @JsonProperty("release_year_to")
    private Date releaseYearTo;

    public SearchByParamsRequestDTO(BigDecimal priceFrom, BigDecimal priceTo, String[] categoriesHierarchy, Date releaseYearFrom, Date releaseYearTo) {
        this.priceFrom = priceFrom;
        this.priceTo = priceTo;
        this.categoriesHierarchy = categoriesHierarchy;
        this.releaseYearFrom = releaseYearFrom;
        this.releaseYearTo = releaseYearTo;
    }
}
