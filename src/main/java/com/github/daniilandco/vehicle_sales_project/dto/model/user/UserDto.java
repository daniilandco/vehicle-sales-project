package com.github.daniilandco.vehicle_sales_project.dto.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.daniilandco.vehicle_sales_project.model.user.Role;
import com.github.daniilandco.vehicle_sales_project.model.user.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String secondName;
    private String phoneNumber;
    private String location;
    private Timestamp registeredAt;
    private Timestamp lastLogin;
    private Status status;
    private Role role;
}
