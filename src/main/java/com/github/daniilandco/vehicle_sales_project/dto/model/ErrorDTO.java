package com.github.daniilandco.vehicle_sales_project.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Class which represents error info.
 *
 * @author com.github.daniilandco
 * @version 1.0
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class ErrorDTO {
    private final String status;
    private final String title;
    private final String details;
}