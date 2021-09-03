package com.github.daniilandco.vehicle_sales_project.service.user;

import com.github.daniilandco.vehicle_sales_project.controller.request.LoginRequest;
import com.github.daniilandco.vehicle_sales_project.controller.request.RegisterRequest;
import com.github.daniilandco.vehicle_sales_project.dto.mapper.UserMapper;
import com.github.daniilandco.vehicle_sales_project.dto.model.user.UserDto;
import com.github.daniilandco.vehicle_sales_project.exception.JwtAuthenticationException;
import com.github.daniilandco.vehicle_sales_project.model.user.User;
import com.github.daniilandco.vehicle_sales_project.repository.user.UserRepository;
import com.github.daniilandco.vehicle_sales_project.security.AuthContextHandler;
import com.github.daniilandco.vehicle_sales_project.security.jwt.JwtTokenProvider;
import com.github.daniilandco.vehicle_sales_project.service.gcs.GenerateV4GetObjectSignedUrl;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.stream.StreamSupport;

@Service
public class UserServiceImplementation implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final Storage storage;
    private final UserMapper userMapper;
    private final GenerateV4GetObjectSignedUrl generateV4GetObjectSignedUrl;
    private final AuthContextHandler authContextHandler;

    @Value("${cloud.bucketname}")
    private String bucketName;
    @Value("${cloud.default.photo.extension}")
    private String defaultExtension;
    @Value("${cloud.subdirectory.profile}")
    private String profilePhotosPath;


    public UserServiceImplementation(PasswordEncoder passwordEncoder, UserRepository userRepository, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, Storage storage, UserMapper userMapper, GenerateV4GetObjectSignedUrl generateV4GetObjectSignedUrl, AuthContextHandler authContextHandler) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.storage = storage;
        this.userMapper = userMapper;
        this.generateV4GetObjectSignedUrl = generateV4GetObjectSignedUrl;
        this.authContextHandler = authContextHandler;
    }

    @Override
    public Iterable<UserDto> getAllUsers() {
        return userMapper.toUserDtoSet(userRepository.findAll());
    }

    @Override
    public UserDto getUserById(Long id) {
        UserDto userDto = userMapper.toUserDto(Objects.requireNonNull(StreamSupport.stream(userRepository.findAll().spliterator(), false).
                filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null)));
        return userDto;
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public String login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("user doesn't exists"));
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getId().toString(), request.getPassword()));

        user.setLastLogin(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);

        String token = jwtTokenProvider.createToken(user.getId());
        return token;
    }

    @Override
    public void register(RegisterRequest request) throws JwtAuthenticationException {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new JwtAuthenticationException("email already exists");
        }
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new JwtAuthenticationException("phone number already exists");
        }

        userRepository.save(getUserFromRegisterRequest(request));
    }

    public User getUserFromRegisterRequest(RegisterRequest request) {
        return new User(
                request.getFirstName(), request.getSecondName(),
                request.getEmail(), request.getPhoneNumber(),
                passwordEncoder.encode(request.getPassword()),
                request.getStatus(), request.getRole()
        );
    }

    @Override
    public void updateProfile(RegisterRequest request) throws Exception {
        User userModel = authContextHandler.getLoggedInUser();

        if (request.getFirstName() != null) {
            userModel.setFirstName(request.getFirstName());
        }
        if (request.getSecondName() != null) {
            userModel.setSecondName(request.getSecondName());
        }
        if (request.getEmail() != null) {
            userModel.setEmail(request.getEmail());
        }
        if (request.getPhoneNumber() != null) {
            userModel.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getPassword() != null) {
            userModel.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getLocation() != null) {
            userModel.setLocation(request.getLocation());
        }
        if (request.getProfilePhoto() != null) {
            userModel.setProfilePhoto(request.getProfilePhoto());
        }

        userRepository.save(userModel);
    }

    @Override
    public void updateProfilePhoto(MultipartFile imageFile) throws IOException, JwtAuthenticationException {
        User userModel = authContextHandler.getLoggedInUser();

        if (userModel.getProfilePhoto() != null) {
            storage.delete(BlobId.of(bucketName, userModel.getProfilePhoto()));
        }

        BlobId blobId = BlobId.of(bucketName, getUniqueProfilePhotoPath(userModel));
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        byte[] bytes = imageFile.getBytes();
        storage.create(blobInfo, bytes);

        userModel.setProfilePhoto(getUniqueProfilePhotoPath(userModel));
        userRepository.save(userModel);
    }

    @Override
    public void deleteProfilePhoto() throws JwtAuthenticationException {
        User userModel = authContextHandler.getLoggedInUser();
        if (userModel.getProfilePhoto() != null) {
            storage.delete(BlobId.of(bucketName, userModel.getProfilePhoto()));
            userModel.setProfilePhoto(null);
            userRepository.save(userModel);
        }
    }

    @Override
    public URL getProfilePhoto() throws JwtAuthenticationException {
        User userModel = authContextHandler.getLoggedInUser();
        return generateV4GetObjectSignedUrl.generate(bucketName, getUniqueProfilePhotoPath(userModel));
    }

    private String getUniqueProfilePhotoPath(User user) {
        return profilePhotosPath + user.getId() + defaultExtension;
    }
}
