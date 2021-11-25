package com.github.daniilandco.vehicle_sales_project.service.user;

import com.github.daniilandco.vehicle_sales_project.controller.request.LoginRequest;
import com.github.daniilandco.vehicle_sales_project.controller.request.RegisterRequest;
import com.github.daniilandco.vehicle_sales_project.controller.response.SuccessAuthResponse;
import com.github.daniilandco.vehicle_sales_project.dto.model.user.UserDto;
import com.github.daniilandco.vehicle_sales_project.exception.auth.*;
import com.github.daniilandco.vehicle_sales_project.exception.image.InvalidImageSizeException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Daniel Bondarkov.
 */
public interface UserService {
    /**
     * Register a new user
     *
     * @return
     */
    SuccessAuthResponse register(RegisterRequest request) throws JwtAuthenticationException, EmailAlreadyExistsException, PhoneNumberAlreadyExistsException, RegistrationException, UserIsNotLoggedInException;

    Iterable<UserDto> getAllUsers();

    UserDto getUserById(Long id);

    void deleteUserById(Long id);

    /**
     * Login a user
     *
     * @return
     */
    SuccessAuthResponse login(LoginRequest request) throws UserIsNotLoggedInException;

    /**
     * Login a user
     */
    void logout(String token, HttpServletRequest request, HttpServletResponse response);

    /**
     * Update profile of the user
     */
    void updateProfile(RegisterRequest request) throws UserIsNotLoggedInException;

    void updateProfilePhoto(byte[] bytes) throws IOException, JwtAuthenticationException, UserIsNotLoggedInException, InvalidImageSizeException;

    void deleteProfilePhoto() throws JwtAuthenticationException, UserIsNotLoggedInException;

    URL getProfilePhoto() throws IOException, JwtAuthenticationException, UserIsNotLoggedInException;

    boolean activateUser(String code);
}
