package com.github.daniilandco.vehicle_sales_project.service.user;

import com.github.daniilandco.vehicle_sales_project.controller.request.LoginRequest;
import com.github.daniilandco.vehicle_sales_project.controller.request.RegisterRequest;
import com.github.daniilandco.vehicle_sales_project.dto.model.user.UserDto;
import org.springframework.http.ResponseEntity;

/**
 * Created by Daniel Bondarkov.
 */
public interface UserService {
    /**
     * Register a new user
     */
    ResponseEntity<?> register(RegisterRequest request);

    /**
     * Login a user
     */
    ResponseEntity<?> login(LoginRequest request);

    /**
     * Search an existing user
     */
    UserDto findUserByEmail(String email);

    /**
     * Update profile of the user
     */
    UserDto updateProfile(UserDto userDto);

    /**
     * Update password
     */
    UserDto changePassword(UserDto userDto, String newPassword);
}
