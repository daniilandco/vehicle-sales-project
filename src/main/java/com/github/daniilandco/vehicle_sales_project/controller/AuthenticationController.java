package com.github.daniilandco.vehicle_sales_project.controller;

import com.github.daniilandco.vehicle_sales_project.controller.request.LoginRequest;
import com.github.daniilandco.vehicle_sales_project.controller.request.RegisterRequest;
import com.github.daniilandco.vehicle_sales_project.controller.response.RestApiResponse;
import com.github.daniilandco.vehicle_sales_project.controller.response.SuccessAuthResponse;
import com.github.daniilandco.vehicle_sales_project.exception.auth.EmailAlreadyExistsException;
import com.github.daniilandco.vehicle_sales_project.exception.auth.PhoneNumberAlreadyExistsException;
import com.github.daniilandco.vehicle_sales_project.exception.auth.RegistrationException;
import com.github.daniilandco.vehicle_sales_project.exception.auth.UserIsNotLoggedInException;
import com.github.daniilandco.vehicle_sales_project.exception.cookie.CookieNotFoundException;
import com.github.daniilandco.vehicle_sales_project.exception.token.InvalidTokenException;
import com.github.daniilandco.vehicle_sales_project.service.user.UserServiceImplementation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserServiceImplementation userService;

    private static final String REFRESH_TOKEN_NAME = "refresh_token";
    @Value("${jwt.refresh.expiration}")
    private int refreshValidityInSeconds;

    public AuthenticationController(UserServiceImplementation userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        try {
            SuccessAuthResponse tokens = userService.login(request);
            setTokenCookie(tokens.getToken().getRefreshToken(),
                    REFRESH_TOKEN_NAME, response);
            return ResponseEntity.ok(new RestApiResponse("user is logged in", tokens));
        } catch (UsernameNotFoundException | UserIsNotLoggedInException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new RestApiResponse(e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            Cookie cookie = getTokenCookie(request, REFRESH_TOKEN_NAME);
            String refreshToken = cookie.getValue();
            userService.logout(refreshToken, request, response);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            return ResponseEntity.ok(new RestApiResponse("user is logged out"));
        } catch (CookieNotFoundException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new RestApiResponse(e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request, HttpServletResponse response) {
        try {
            SuccessAuthResponse tokens = userService.register(request);
            setTokenCookie(tokens.getToken().getRefreshToken(),
                    REFRESH_TOKEN_NAME, response);
            return ResponseEntity.ok(new RestApiResponse("user is registered", tokens));
        } catch (EmailAlreadyExistsException | PhoneNumberAlreadyExistsException | RegistrationException | UserIsNotLoggedInException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new RestApiResponse(e.getMessage()));
        }
    }

    @PutMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
        try {
            Cookie cookie = getTokenCookie(request, REFRESH_TOKEN_NAME);
            String refreshToken = cookie.getValue();
            SuccessAuthResponse authResponse = userService.refresh(refreshToken);
            String newRefreshToken = authResponse.getToken().getRefreshToken();
            setTokenCookie(newRefreshToken, REFRESH_TOKEN_NAME, response);
            return ResponseEntity.ok(new RestApiResponse("refresh token is up to date", authResponse.getToken()));
        } catch (UserIsNotLoggedInException | CookieNotFoundException | InvalidTokenException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new RestApiResponse(e.getMessage()));
        }
    }

    @GetMapping("/activate/{code}")
    public ResponseEntity<?> activate(@PathVariable String code) {
        boolean isActivated = userService.activateUser(code);
        return isActivated ? ResponseEntity.ok(new RestApiResponse("successful activation")) :
                ResponseEntity
                        .badRequest()
                        .body(new RestApiResponse("unsuccessful activation"));
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
