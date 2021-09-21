package com.github.daniilandco.vehicle_sales_project.controller.request;

import lombok.Data;
import org.apache.commons.lang3.Range;

import java.math.BigDecimal;
import java.sql.Date;

@Data
public class SearchByParamsRequest {
    private Range<BigDecimal> priceRange;
    private String[] categoriesHierarchy;
    private Range<Date> releaseYearRange;
}
