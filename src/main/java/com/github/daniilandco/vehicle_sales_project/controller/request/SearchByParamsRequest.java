package com.github.daniilandco.vehicle_sales_project.controller.request;

import lombok.Data;
import org.springframework.data.util.Pair;

import java.math.BigDecimal;
import java.sql.Date;

@Data
public class SearchByParamsRequest {
    private Pair<BigDecimal, BigDecimal> priceRange;
    private String[] categoriesHierarchy;
    private Pair<Date, Date> releaseYearRange;

    public SearchByParamsRequest(Pair<BigDecimal, BigDecimal> priceRange, String[] categoriesHierarchy, Pair<Date, Date> releaseYearRange) {
        this.priceRange = priceRange;
        this.categoriesHierarchy = categoriesHierarchy;
        this.releaseYearRange = releaseYearRange;
    }
}
