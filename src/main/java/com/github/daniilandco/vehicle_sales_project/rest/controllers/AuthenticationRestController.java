package com.github.daniilandco.vehicle_sales_project.rest.controllers;

import com.github.daniilandco.vehicle_sales_project.config.SecurityConfig;
import com.github.daniilandco.vehicle_sales_project.database_access.user.User;
import com.github.daniilandco.vehicle_sales_project.database_access.user.UserRepository;
import com.github.daniilandco.vehicle_sales_project.rest.login.LoginRequest;
import com.github.daniilandco.vehicle_sales_project.rest.login.LoginResponse;
import com.github.daniilandco.vehicle_sales_project.rest.register.RegisterRequest;
import com.github.daniilandco.vehicle_sales_project.rest.register.RegisterResponse;
import com.github.daniilandco.vehicle_sales_project.security.jwt.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationRestController {
    private final AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private JwtTokenProvider jwtTokenProvider;

    public AuthenticationRestController(AuthenticationManager authenticationManager, UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
        String token = jwtTokenProvider.createToken(request.getEmail(), user.getRole().name());
        return ResponseEntity.ok(new LoginResponse(token, request.getEmail()));
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new RegisterResponse("Error: Username already exists"));
        }

        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            return ResponseEntity
                    .badRequest()
                    .body(new RegisterResponse("Error: Phone number already exists"));
        }

        User user = new User(
                request.getFirstName(), request.getSecondName(),
                request.getEmail(), request.getPhoneNumber(),
                SecurityConfig.passwordEncoder().encode(request.getPassword()),
                request.getStatus(), request.getRole()
        );

        userRepository.save(user);

        return ResponseEntity.ok(new RegisterResponse("User is registered"));
    }
}