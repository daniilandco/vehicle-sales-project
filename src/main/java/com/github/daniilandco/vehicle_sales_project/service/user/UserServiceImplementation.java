package com.github.daniilandco.vehicle_sales_project.service.user;

import com.github.daniilandco.vehicle_sales_project.controller.request.LoginRequest;
import com.github.daniilandco.vehicle_sales_project.controller.request.RegisterRequest;
import com.github.daniilandco.vehicle_sales_project.controller.response.RestApiResponse;
import com.github.daniilandco.vehicle_sales_project.controller.response.SuccessLoginResponse;
import com.github.daniilandco.vehicle_sales_project.dto.mapper.UserMapper;
import com.github.daniilandco.vehicle_sales_project.dto.model.user.UserDto;
import com.github.daniilandco.vehicle_sales_project.model.user.User;
import com.github.daniilandco.vehicle_sales_project.repository.user.UserRepository;
import com.github.daniilandco.vehicle_sales_project.security.UserDetailsServiceImplementation;
import com.github.daniilandco.vehicle_sales_project.security.jwt.JwtTokenProvider;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class UserServiceImplementation implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserDetailsServiceImplementation userDetailsService;
    @Autowired
    private Storage storage;

    @Value("${cloud.bucketname}")
    private String bucketName;
    @Value("${cloud.default.photo.extension}")
    private String defaultExtension;
    @Value("${cloud.subdirectory.profile}")
    private String profilePhotosPath;


    public UserServiceImplementation(PasswordEncoder passwordEncoder, UserRepository userRepository, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity
                .ok(new RestApiResponse(HttpStatus.OK.value(),
                        "", UserMapper.toUserDtoSet(userRepository.findAll())));
    }

    @Override
    public ResponseEntity<?> getUserById(Long id) {
        UserDto userDto = UserMapper.toUserDto(Objects.requireNonNull(StreamSupport.stream(userRepository.findAll().spliterator(), false).
                filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null)));
        return ResponseEntity
                .ok(new RestApiResponse(HttpStatus.OK.value(),
                        "", userDto));
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public ResponseEntity<?> login(LoginRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(), request.getPassword()));
        } catch (AuthenticationException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new RestApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            e.getMessage()));
        }

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
        user.setLastLogin(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);

        String token = jwtTokenProvider.createToken(request.getEmail());
        return ResponseEntity.ok(
                new RestApiResponse(HttpStatus.OK.value(),
                        "User is logged in",
                        new SuccessLoginResponse(token, request.getEmail())));
    }

    @Override
    public ResponseEntity<?> register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new RestApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Email already exists"));
        }
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            return ResponseEntity
                    .badRequest()
                    .body(new RestApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Phone number already exists"));
        }

        try {
            userRepository.save(getUserFromRegisterRequest(request));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new RestApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Registration error"));
        }

        return ResponseEntity.ok(new RestApiResponse(HttpStatus.OK.value(),
                "User is registered"));
    }

    public User getUserFromRegisterRequest(RegisterRequest request) {
        return new User(
                request.getFirstName(), request.getSecondName(),
                request.getEmail(), request.getPhoneNumber(),
                passwordEncoder.encode(request.getPassword()),
                request.getStatus(), request.getRole()
        );
    }

    /**
     * Search an existing user
     */
    @Transactional
    public UserDto findUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return UserMapper.toUserDto(user.get()); //handle exception
    }

    @Override
    public ResponseEntity<?> updateProfile(RegisterRequest request) {
        Optional<User> user = getLoggedInUser();
        if (user.isPresent()) {
            User userModel = user.get();

            if (request.getFirstName() != null) userModel.setFirstName(request.getFirstName());
            if (request.getSecondName() != null) userModel.setSecondName(request.getSecondName());
            if (request.getEmail() != null) userModel.setEmail(request.getEmail());
            if (request.getPhoneNumber() != null) userModel.setPhoneNumber(request.getPhoneNumber());
            if (request.getPassword() != null) userModel.setPassword(passwordEncoder.encode(request.getPassword()));
            if (request.getLocation() != null) userModel.setLocation(request.getLocation());
            if (request.getProfilePhoto() != null) userModel.setProfilePhoto(request.getProfilePhoto());

            userRepository.save(userModel);
            updateContext(userModel);

            return ResponseEntity
                    .ok(new RestApiResponse(HttpStatus.OK.value(),
                            "profile is updated"));
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new RestApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Update profile error"));
        }
    }

    @Override
    public ResponseEntity<?> updateProfilePhoto(MultipartFile imageFile) throws IOException {
        Optional<User> user = getLoggedInUser();
        if (user.isPresent()) {
            User userModel = user.get();

            if (userModel.getProfilePhoto() != null) {
                storage.delete(BlobId.of(bucketName, profilePhotosPath + getUniqueProfilePhotoName(userModel)));
            }

            BlobId blobId = BlobId.of(bucketName, profilePhotosPath + getUniqueProfilePhotoName(userModel));
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
            byte[] bytes = imageFile.getBytes();
            storage.create(blobInfo, bytes);

            userModel.setProfilePhoto(getUniqueProfilePhotoName(userModel));
            userRepository.save(userModel);
            return ResponseEntity.ok(new RestApiResponse(HttpStatus.OK.value(), "Profile photo is updated"));
        } else {
            return ResponseEntity
                    .badRequest()
                    .body((new RestApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Update profile photo error")));
        }
    }

    private String getUniqueProfilePhotoName(User user) {
        return user.getId() + defaultExtension;
    }

    @Override
    public ResponseEntity<?> getProfilePhoto() throws IOException {
        Optional<User> user = getLoggedInUser();
        if (user.isPresent()) {
            User userModel = user.get();

            byte[] bytes = storage.readAllBytes(bucketName, profilePhotosPath + getUniqueProfilePhotoName(userModel));

            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            BufferedImage bImage2 = ImageIO.read(bis);

            return ResponseEntity.ok(new RestApiResponse(HttpStatus.OK.value(), "ok", bImage2));
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new RestApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error"));
        }
    }

    @Override
    public UserDto changePassword(UserDto userDto, String newPassword) {
        return null;
    }

    private void updateContext(User user) {
        Authentication authentication = getCurrentAuthentication(user.getEmail());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Authentication getCurrentAuthentication(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), "", userDetails.getAuthorities());
    }

    private Optional<User> getLoggedInUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return userRepository.findByEmail(email);
    }
}
