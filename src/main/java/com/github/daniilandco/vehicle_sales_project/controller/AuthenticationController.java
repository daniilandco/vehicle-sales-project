package com.github.daniilandco.vehicle_sales_project.controller;

import com.github.daniilandco.vehicle_sales_project.controller.request.LoginRequest;
import com.github.daniilandco.vehicle_sales_project.controller.request.RegisterRequest;
import com.github.daniilandco.vehicle_sales_project.controller.response.RestApiResponse;
import com.github.daniilandco.vehicle_sales_project.exception.auth.EmailAlreadyExistsException;
import com.github.daniilandco.vehicle_sales_project.exception.auth.PhoneNumberAlreadyExistsException;
import com.github.daniilandco.vehicle_sales_project.exception.auth.RegistrationException;
import com.github.daniilandco.vehicle_sales_project.service.user.UserServiceImplementation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserServiceImplementation userService;

    public AuthenticationController(UserServiceImplementation userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            String token = userService.login(request);
            return ResponseEntity.ok(new RestApiResponse("user is logged in", token));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new RestApiResponse(e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        userService.logout(request, response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            return ResponseEntity.ok(new RestApiResponse("user is registered", userService.register(request)));
        } catch (EmailAlreadyExistsException | PhoneNumberAlreadyExistsException | RegistrationException e) {
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
}
