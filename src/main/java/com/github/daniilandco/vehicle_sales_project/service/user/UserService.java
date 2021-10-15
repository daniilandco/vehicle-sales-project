package com.github.daniilandco.vehicle_sales_project.service.user;

import com.github.daniilandco.vehicle_sales_project.controller.request.LoginRequest;
import com.github.daniilandco.vehicle_sales_project.controller.request.RegisterRequest;
import com.github.daniilandco.vehicle_sales_project.dto.model.user.UserDto;
import com.github.daniilandco.vehicle_sales_project.exception.*;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Daniel Bondarkov.
 */
public interface UserService {
    /**
     * Register a new user
     */
    UserDto register(RegisterRequest request) throws JwtAuthenticationException, EmailAlreadyExistsException, PhoneNumberAlreadyExistsException, RegistrationException;

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
    void updateProfile(RegisterRequest request) throws UserIsNotLoggedInException;

    void updateProfilePhoto(byte[] bytes) throws IOException, JwtAuthenticationException, UserIsNotLoggedInException, InvalidImageSizeException;

    void deleteProfilePhoto() throws JwtAuthenticationException, UserIsNotLoggedInException;

    URL getProfilePhoto() throws IOException, JwtAuthenticationException, UserIsNotLoggedInException;

}
