package com.github.daniilandco.vehicle_sales_project.rest;

import lombok.Data;

@Data
public class AuthenticationResponse {
    private String email;
    private String token;
    private final String type = "Bearer";

    public AuthenticationResponse(String token, String email) {
        this.token = token;
        this.email = email;
    }
}
