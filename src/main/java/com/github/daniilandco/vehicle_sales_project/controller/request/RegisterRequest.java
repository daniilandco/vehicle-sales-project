package com.github.daniilandco.vehicle_sales_project.controller.request;

import com.github.daniilandco.vehicle_sales_project.model.user.Role;
import com.github.daniilandco.vehicle_sales_project.model.user.Status;
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
    private String location;
    private String profilePhoto;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Enumerated(value = EnumType.STRING)
    private Status status;

}
