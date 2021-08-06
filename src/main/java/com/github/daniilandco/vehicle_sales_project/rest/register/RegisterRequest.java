package com.github.daniilandco.vehicle_sales_project.rest.register;

import com.github.daniilandco.vehicle_sales_project.database_access.user.user_model.Role;
import com.github.daniilandco.vehicle_sales_project.database_access.user.user_model.Status;
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
