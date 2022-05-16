package com.github.daniilandco.vehicle_sales_project.controller;

import com.github.daniilandco.vehicle_sales_project.dto.request.LoginRequestDTO;
import com.github.daniilandco.vehicle_sales_project.dto.request.RegisterRequestDTO;
import com.github.daniilandco.vehicle_sales_project.dto.response.RestApiResponse;
import com.github.daniilandco.vehicle_sales_project.dto.response.SuccessLoginResponse;
import com.github.daniilandco.vehicle_sales_project.exception.auth.*;
import com.github.daniilandco.vehicle_sales_project.exception.cookie.CookieNotFoundException;
import com.github.daniilandco.vehicle_sales_project.exception.token.InvalidTokenException;
import com.github.daniilandco.vehicle_sales_project.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;

    private static final String REFRESH_TOKEN_NAME = "refresh_token";
    @Value("${jwt.refresh.expiration}")
    private int refreshValidityInSeconds;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request, HttpServletResponse response) throws UserIsNotLoggedInException {
        SuccessLoginResponse tokens = userService.login(request);
        setTokenCookie(tokens.getToken().getRefreshToken(), REFRESH_TOKEN_NAME, response);

        return ResponseEntity.ok(new RestApiResponse("user is logged in", tokens));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) throws CookieNotFoundException {
        Cookie cookie = getTokenCookie(request, REFRESH_TOKEN_NAME);
        String refreshToken = cookie.getValue();

        userService.logout(refreshToken, request, response);

        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok(new RestApiResponse("user is logged out"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO request, HttpServletResponse response) throws RegistrationException, EmailAlreadyExistsException, UserIsNotLoggedInException, JwtAuthenticationException, PhoneNumberAlreadyExistsException {
        return ResponseEntity.ok(new RestApiResponse("user is registered", userService.register(request)));
    }

    @PutMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) throws InvalidTokenException, UserIsNotLoggedInException, CookieNotFoundException {
        Cookie cookie = getTokenCookie(request, REFRESH_TOKEN_NAME);
        String refreshToken = cookie.getValue();
        SuccessLoginResponse authResponse = userService.refresh(refreshToken);
        String newRefreshToken = authResponse.getToken().getRefreshToken();
        setTokenCookie(newRefreshToken, REFRESH_TOKEN_NAME, response);

        return ResponseEntity.ok(new RestApiResponse("refresh token is up to date", authResponse.getToken()));
    }

    @GetMapping("/activate/{code}")
    public ResponseEntity<?> activate(@PathVariable String code) throws UnsuccessfulActivationException {
        userService.activateUser(code);

        return ResponseEntity.ok(new RestApiResponse("successful activation", code));

    }

    private void setTokenCookie(String token, String cookieName, HttpServletResponse response) {
        Cookie cookie = new Cookie(cookieName, token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(refreshValidityInSeconds);
        response.addCookie(cookie);
    }

    private Cookie getTokenCookie(HttpServletRequest request, String cookieName) throws CookieNotFoundException {
        Cookie cookie = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals(cookieName))
                .findFirst().orElseThrow(() -> new CookieNotFoundException("cookie with name" + cookieName + "not found"));
        return cookie;
    }
}
