package com.github.daniilandco.vehicle_sales_project.model.token;

import com.github.daniilandco.vehicle_sales_project.model.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "token")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "token")
    private String refreshToken;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Token() {
    }

    public Token(String refreshToken, User user) {
        this.refreshToken = refreshToken;
        this.user = user;
    }
}
