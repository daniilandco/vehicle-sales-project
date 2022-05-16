package com.github.daniilandco.vehicle_sales_project.repository;

import com.github.daniilandco.vehicle_sales_project.model.token.Token;
import com.github.daniilandco.vehicle_sales_project.model.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenRepository extends CrudRepository<Token, Long> {
    Optional<Token> findByUser(User user);

    void deleteByRefreshToken(String refreshToken);

    boolean existsByRefreshToken(String refreshToken);
}
