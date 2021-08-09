package com.github.daniilandco.vehicle_sales_project.rest.auth.register;

import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class RegisterResponse {
    @Enumerated(EnumType.STRING)
    private int response;
    private String message;

    public RegisterResponse(int responseStatus, String message) {
        this.message = message;
        this.response = responseStatus;
    }

}
