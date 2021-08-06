package com.github.daniilandco.vehicle_sales_project.rest;

import lombok.Data;

@Data
public class RegisterResponse {
    private String message;

    public RegisterResponse(String message) {
        this.message = message;
    }
}
