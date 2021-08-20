package com.github.daniilandco.vehicle_sales_project.controller.response;

import lombok.Data;

@Data
public class SuccessLoginResponse {

    private String email;
    private String token;
    private final String type = "Bearer";

    public SuccessLoginResponse(String token, String email) {
        this.token = token;
        this.email = email;
    }
}
