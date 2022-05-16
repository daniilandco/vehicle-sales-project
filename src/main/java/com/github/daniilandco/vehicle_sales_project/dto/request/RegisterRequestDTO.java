package com.github.daniilandco.vehicle_sales_project.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.daniilandco.vehicle_sales_project.model.user.Role;
import com.github.daniilandco.vehicle_sales_project.model.user.Status;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public record RegisterRequestDTO(
        @JsonProperty("first_name")
        String firstName,
        @JsonProperty("second_name")
        String secondName,
        String email,
        @JsonProperty("phone_number")
        String phoneNumber,
        String password,
        String location,
        @JsonProperty("profile_photo")
        String profilePhoto,
        @Enumerated(EnumType.STRING)
        Role role,
        @Enumerated(EnumType.STRING)
        Status status
) {
}
