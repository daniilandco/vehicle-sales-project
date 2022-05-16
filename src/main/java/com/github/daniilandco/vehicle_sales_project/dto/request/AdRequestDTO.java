package com.github.daniilandco.vehicle_sales_project.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.daniilandco.vehicle_sales_project.model.ad.Status;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.sql.Date;


public record AdRequestDTO(
        String title,
        String description,
        BigDecimal price,
        @JsonProperty("categories_hierarchy")
        String[] categoriesHierarchy,
        @JsonProperty("release_year")
        Date releaseYear,
        @Enumerated(EnumType.STRING)
        Status status
) {
}
