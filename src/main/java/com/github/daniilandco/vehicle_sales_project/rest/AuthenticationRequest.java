package com.github.daniilandco.vehicle_sales_project.rest;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String email;
    private String password;
}