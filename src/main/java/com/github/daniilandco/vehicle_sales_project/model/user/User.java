package com.github.daniilandco.vehicle_sales_project.model.user;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.github.daniilandco.vehicle_sales_project.model.ad.Ad;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serial;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Set;

@Entity(name = "User") // Tells Hibernate to make a table out of this class
@Table(name = "user",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email", name = "email"),
                @UniqueConstraint(columnNames = "phone_number", name = "phone_number")
        })
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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

    @JsonManagedReference
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Ad> ads;

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

    @JsonIgnore
    public boolean isActive() {
        return status.equals(Status.ACTIVE);
    }

    @JsonIgnore
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

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getRole().getAuthorities();
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return getEmail();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return isActive();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return isActive();
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return isActive();
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return isActive();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Timestamp getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(Timestamp registeredAt) {
        this.registeredAt = registeredAt;
    }

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Set<Ad> getAds() {
        return ads;
    }

    public void setAds(Set<Ad> ads) {
        this.ads = ads;
    }
}
