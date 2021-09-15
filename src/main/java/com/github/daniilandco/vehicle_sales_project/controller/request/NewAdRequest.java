package com.github.daniilandco.vehicle_sales_project.controller.request;

import com.github.daniilandco.vehicle_sales_project.model.ad.Status;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.sql.Date;

@Data
public class NewAdRequest {
    private String title;
    private String description;
    private BigDecimal price;
    private String[] categoriesHierarchy;
    private Date releaseYear;

    @Enumerated(EnumType.STRING)
    private Status status;
}
