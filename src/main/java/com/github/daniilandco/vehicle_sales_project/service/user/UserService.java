package com.github.daniilandco.vehicle_sales_project.service.user;

import com.github.daniilandco.vehicle_sales_project.controller.request.LoginRequest;
import com.github.daniilandco.vehicle_sales_project.controller.request.RegisterRequest;
import com.github.daniilandco.vehicle_sales_project.dto.model.user.UserDto;
import com.github.daniilandco.vehicle_sales_project.exception.JwtAuthenticationException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Daniel Bondarkov.
 */
public interface UserService {
    /**
     * Register a new user
     */
    void register(RegisterRequest request) throws JwtAuthenticationException;

    Iterable<UserDto> getAllUsers();

    UserDto getUserById(Long id);

    void deleteUserById(Long id);

    /**
     * Login a user
     */
    String login(LoginRequest request);

    /**
     * Update profile of the user
     */
    void updateProfile(RegisterRequest request) throws Exception;

    void updateProfilePhoto(MultipartFile imageFile) throws IOException, JwtAuthenticationException;

    void deleteProfilePhoto() throws JwtAuthenticationException;

    URL getProfilePhoto() throws IOException, JwtAuthenticationException;

}
