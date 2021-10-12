package com.github.daniilandco.vehicle_sales_project.controller.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class SearchByParamsRequest extends PagePaginationRequest {
    private BigDecimal priceFrom;
    private BigDecimal priceTo;

    private String[] categoriesHierarchy;

    private Date releaseYearFrom;
    private Date releaseYearTo;

    public SearchByParamsRequest(BigDecimal priceFrom, BigDecimal priceTo, String[] categoriesHierarchy, Date releaseYearFrom, Date releaseYearTo) {
        this.priceFrom = priceFrom;
        this.priceTo = priceTo;
        this.categoriesHierarchy = categoriesHierarchy;
        this.releaseYearFrom = releaseYearFrom;
        this.releaseYearTo = releaseYearTo;
    }
}
