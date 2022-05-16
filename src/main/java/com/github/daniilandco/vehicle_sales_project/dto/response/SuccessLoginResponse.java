package com.github.daniilandco.vehicle_sales_project.dto.response;

import com.github.daniilandco.vehicle_sales_project.dto.model.UserDTO;
import com.github.daniilandco.vehicle_sales_project.security.jwt.TokenResponse;
import lombok.Data;

@Data
public class SuccessLoginResponse {
    private static final String type = "Bearer";
    private UserDTO user;
    private TokenResponse token;

    public SuccessLoginResponse(UserDTO user, TokenResponse token) {
        this.user = user;
        this.token = token;
    }
}
