package com.github.daniilandco.vehicle_sales_project.security;

import com.github.daniilandco.vehicle_sales_project.exception.auth.UserIsNotLoggedInException;
import com.github.daniilandco.vehicle_sales_project.model.user.User;
import com.github.daniilandco.vehicle_sales_project.repository.user.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthContextHandler {
    private final UserRepository userRepository;

    public AuthContextHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getLoggedInUser() throws UserIsNotLoggedInException {
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UserIsNotLoggedInException();
        } else {
            return user.get();
        }
    }
}
