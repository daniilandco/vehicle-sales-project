package com.github.daniilandco.vehicle_sales_project.dto.model.ad;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.daniilandco.vehicle_sales_project.model.ad.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

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
public class AdDto {
    private Long id;
    //private UserDto owner;
    private String title;
    private String description;
    private Integer makeId;
    private BigDecimal price;
    private Timestamp createdAt;
    private Date releaseYear;
    private Status status;
}
