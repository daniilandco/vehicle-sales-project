package com.github.daniilandco.vehicle_sales_project.model.user;

import com.github.daniilandco.vehicle_sales_project.model.ad.Ad;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Set;

@Getter
@Setter
@Entity(name = "User") // Tells Hibernate to make a table out of this class
@Table(name = "User",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email", name = "email"),
                @UniqueConstraint(columnNames = "phone_number", name = "phone_number")
        })
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
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

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Ad> ads;

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

    public UserDetails getUserDetails() {
        return new org.springframework.security.core.userdetails.User(
                this.getId().toString(), this.getPassword(),
                this.isActive(),
                this.isActive(),
                this.isActive(),
                this.isActive(),
                this.getRole().getAuthorities()
        );
    }

    public boolean isActive() {
        return status.equals(Status.ACTIVE);
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
        return getId().toString();
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
