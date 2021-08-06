package com.github.daniilandco.vehicle_sales_project.rest.login;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}