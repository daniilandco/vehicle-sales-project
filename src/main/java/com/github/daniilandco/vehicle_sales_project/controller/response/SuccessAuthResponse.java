package com.github.daniilandco.vehicle_sales_project.controller.response;

import com.github.daniilandco.vehicle_sales_project.dto.model.user.UserDto;
import com.github.daniilandco.vehicle_sales_project.security.jwt.TokenResponse;
import lombok.Data;

@Data
public class SuccessAuthResponse {

    private final String type = "Bearer";
    private UserDto user;
    private TokenResponse token;

    public SuccessAuthResponse(UserDto user, TokenResponse token) {
        this.user = user;
        this.token = token;
    }
}
