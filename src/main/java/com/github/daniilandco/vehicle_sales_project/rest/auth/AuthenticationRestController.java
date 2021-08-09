package com.github.daniilandco.vehicle_sales_project.rest.auth;

import com.github.daniilandco.vehicle_sales_project.database_access.user.User;
import com.github.daniilandco.vehicle_sales_project.database_access.user.UserRepository;
import com.github.daniilandco.vehicle_sales_project.rest.auth.login.LoginRequest;
import com.github.daniilandco.vehicle_sales_project.rest.auth.login.LoginResponse;
import com.github.daniilandco.vehicle_sales_project.rest.auth.register.RegisterRequest;
import com.github.daniilandco.vehicle_sales_project.rest.auth.register.RegisterResponse;
import com.github.daniilandco.vehicle_sales_project.security.jwt.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;

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
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(), request.getPassword()));
        } catch (AuthenticationException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
        user.setLastLogin(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);

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
                    .body(new RegisterResponse(HttpServletResponse.SC_BAD_REQUEST, "Email already exists"));
        }
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            return ResponseEntity
                    .badRequest()
                    .body(new RegisterResponse(HttpServletResponse.SC_BAD_REQUEST, "Phone number already exists"));
        }

        try {
            userRepository.save(request.getUser());
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new RegisterResponse(HttpServletResponse.SC_BAD_REQUEST, "Registration error"));
        }

        return ResponseEntity.ok(new RegisterResponse(HttpServletResponse.SC_CREATED, "User is registered"));
    }
}