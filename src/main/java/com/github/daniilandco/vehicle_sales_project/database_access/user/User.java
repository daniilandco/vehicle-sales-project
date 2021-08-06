package com.github.daniilandco.vehicle_sales_project.database_access.user;

import com.github.daniilandco.vehicle_sales_project.user_model.Role;
import com.github.daniilandco.vehicle_sales_project.user_model.Status;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data // Creates getters and setters for all fields
@Entity // This tells Hibernate to make a table out of this class
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "phone_number")
        })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "second_name")
    private String secondName;

    @Column(name = "profile_photo")
    private String profilePhoto;

    @Column(name = "location")
    private String location;

    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @Column(name = "last_login")
    private Timestamp lastLogin;

    public User() {
    }

    public User(String firstName, String secondName, String email, String phoneNumber, String password, Status status, Role role) {
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.role = role;
        this.firstName = firstName;
        this.secondName = secondName;
        this.registeredAt = this.lastLogin = new Timestamp(System.currentTimeMillis());
        this.location = this.profilePhoto = null;
        this.id = null;
    }
}