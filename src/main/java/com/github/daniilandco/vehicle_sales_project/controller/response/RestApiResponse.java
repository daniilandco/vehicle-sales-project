package com.github.daniilandco.vehicle_sales_project.controller.response;

import lombok.Data;

@Data
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
