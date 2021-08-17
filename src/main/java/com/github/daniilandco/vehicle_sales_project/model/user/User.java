package com.github.daniilandco.vehicle_sales_project.model.user;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import java.io.Serial;
import java.sql.Timestamp;
import java.util.Collection;

@Data // Creates getters and setters for all fields
@Entity // This tells Hibernate to make a table out of this class
@Table(name = "Users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email", name = "email"),
                @UniqueConstraint(columnNames = "phone_number", name = "phone_number")
        })
public class User implements UserDetails {
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
    @CreationTimestamp
    private Timestamp registeredAt;

    @Column(name = "last_login")
    @UpdateTimestamp
    private Timestamp lastLogin;

    @Serial
    @Transient
    private static final long serialVersionUID = 1L;

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
    }

    public boolean isActive() {
        return status.equals(Status.ACTIVE);
    }

    public UserDetails getUserDetails() {
        return new org.springframework.security.core.userdetails.User(
                this.getEmail(), this.getPassword(),
                this.isActive(),
                this.isActive(),
                this.isActive(),
                this.isActive(),
                this.getRole().getAuthorities()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getRole().getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive();
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive();
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }
}
