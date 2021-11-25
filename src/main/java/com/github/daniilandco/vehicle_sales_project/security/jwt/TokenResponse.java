package com.github.daniilandco.vehicle_sales_project.security.jwt;

import lombok.Data;

@Data
public class TokenResponse {
    private String accessToken;
    private String refreshToken;

    public TokenResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
