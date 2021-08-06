package com.github.daniilandco.vehicle_sales_project.rest;

import com.github.daniilandco.vehicle_sales_project.user_model.Role;
import com.github.daniilandco.vehicle_sales_project.user_model.Status;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class RegisterRequest {
    private String firstName;
    private String secondName;
    private String email;
    private String phoneNumber;
    private String password;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Enumerated(value = EnumType.STRING)
    private Status status;

}
