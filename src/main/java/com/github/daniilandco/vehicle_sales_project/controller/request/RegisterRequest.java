package com.github.daniilandco.vehicle_sales_project.controller.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private String firstName;
    private String secondName;
    private String email;
    private String phoneNumber;
    private String password;
    private String location;
    private String profilePhoto;
    private String role;
    private String status;
}
