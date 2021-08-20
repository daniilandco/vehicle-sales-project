package com.github.daniilandco.vehicle_sales_project.controller.response;

import lombok.Data;

@Data
public class RestApiResponse {

    private int status;
    private String message;
    private Object body;

    public RestApiResponse(int status, String message, Object body) {
        this.status = status;
        this.message = message;
        this.body = body;
    }

    public RestApiResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
