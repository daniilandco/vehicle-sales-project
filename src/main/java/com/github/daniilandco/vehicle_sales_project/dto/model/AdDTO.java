package com.github.daniilandco.vehicle_sales_project.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.github.daniilandco.vehicle_sales_project.model.ad.Status;
import com.github.daniilandco.vehicle_sales_project.model.category.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * Created by Daniel Bondarkov.
 */
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdDTO {
    private Long id;
    @JsonManagedReference
    private Category category;
    private String title;
    private String description;
    private BigDecimal price;
    private Date createdAt;
    private Date releaseYear;
    private Status status;
}
