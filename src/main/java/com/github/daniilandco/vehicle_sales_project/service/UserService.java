package com.github.daniilandco.vehicle_sales_project.service;

import com.github.daniilandco.vehicle_sales_project.dto.model.UserDTO;
import com.github.daniilandco.vehicle_sales_project.dto.request.LoginRequestDTO;
import com.github.daniilandco.vehicle_sales_project.dto.request.RegisterRequestDTO;
import com.github.daniilandco.vehicle_sales_project.dto.response.SuccessLoginResponse;
import com.github.daniilandco.vehicle_sales_project.exception.auth.*;
import com.github.daniilandco.vehicle_sales_project.exception.image.InvalidImageSizeException;
import com.github.daniilandco.vehicle_sales_project.exception.token.InvalidTokenException;

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
    UserDTO register(RegisterRequestDTO request) throws JwtAuthenticationException, EmailAlreadyExistsException, PhoneNumberAlreadyExistsException, RegistrationException, UserIsNotLoggedInException;

    Iterable<UserDTO> getAllUsers();

    UserDTO getUserById(Long id);

    void deleteUserById(Long id);

    /**
     * Login a user
     *
     * @return
     */
    SuccessLoginResponse login(LoginRequestDTO request) throws UserIsNotLoggedInException;

    /**
     * Login a user
     */
    void logout(String token, HttpServletRequest request, HttpServletResponse response);

    /**
     * Update profile of the user
     */
    void updateProfile(RegisterRequestDTO request) throws UserIsNotLoggedInException;

    void updateProfilePhoto(byte[] bytes) throws IOException, JwtAuthenticationException, UserIsNotLoggedInException, InvalidImageSizeException;

    void deleteProfilePhoto() throws JwtAuthenticationException, UserIsNotLoggedInException;

    URL getProfilePhoto() throws IOException, JwtAuthenticationException, UserIsNotLoggedInException;

    SuccessLoginResponse refresh(String refreshToken) throws UserIsNotLoggedInException, InvalidTokenException;

    void activateUser(String code) throws UnsuccessfulActivationException;
}
