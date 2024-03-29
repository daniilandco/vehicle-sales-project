package com.github.daniilandco.vehicle_sales_project.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestApiResponse {
    private String message;
    private Object body;

    public RestApiResponse(String message, Object body) {
        this.message = message;
        this.body = body;
    }

    public RestApiResponse(String message) {
        this.message = message;
    }
}
