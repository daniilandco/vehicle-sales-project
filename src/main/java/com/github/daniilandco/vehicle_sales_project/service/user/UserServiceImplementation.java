package com.github.daniilandco.vehicle_sales_project.service.user;

import com.github.daniilandco.vehicle_sales_project.controller.request.LoginRequest;
import com.github.daniilandco.vehicle_sales_project.controller.request.RegisterRequest;
import com.github.daniilandco.vehicle_sales_project.dto.mapper.UserMapper;
import com.github.daniilandco.vehicle_sales_project.dto.model.user.UserDto;
import com.github.daniilandco.vehicle_sales_project.exception.auth.EmailAlreadyExistsException;
import com.github.daniilandco.vehicle_sales_project.exception.auth.PhoneNumberAlreadyExistsException;
import com.github.daniilandco.vehicle_sales_project.exception.auth.RegistrationException;
import com.github.daniilandco.vehicle_sales_project.exception.auth.UserIsNotLoggedInException;
import com.github.daniilandco.vehicle_sales_project.exception.image.InvalidImageSizeException;
import com.github.daniilandco.vehicle_sales_project.model.user.Role;
import com.github.daniilandco.vehicle_sales_project.model.user.Status;
import com.github.daniilandco.vehicle_sales_project.model.user.User;
import com.github.daniilandco.vehicle_sales_project.repository.user.UserRepository;
import com.github.daniilandco.vehicle_sales_project.security.AuthContextHandler;
import com.github.daniilandco.vehicle_sales_project.security.jwt.JwtTokenProvider;
import com.github.daniilandco.vehicle_sales_project.service.gcs.GoogleStorageSignedUrlGenerator;
import com.github.daniilandco.vehicle_sales_project.service.image.ImageService;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    private final GoogleStorageSignedUrlGenerator googleStorageSignedUrlGenerator;
    private final AuthContextHandler authContextHandler;
    private final ImageService imageService;

    @Value("${cloud.bucketname}")
    private String bucketName;
    @Value("${cloud.default.photo.format}")
    private String defaultFormat;
    @Value("${cloud.subdirectory.profile}")
    private String profilePhotosPath;


    public UserServiceImplementation(PasswordEncoder passwordEncoder, UserRepository userRepository, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, Storage storage, UserMapper userMapper, GoogleStorageSignedUrlGenerator googleStorageSignedUrlGenerator, AuthContextHandler authContextHandler, ImageService imageService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.storage = storage;
        this.userMapper = userMapper;
        this.googleStorageSignedUrlGenerator = googleStorageSignedUrlGenerator;
        this.authContextHandler = authContextHandler;
        this.imageService = imageService;
    }

    @Override
    public Iterable<UserDto> getAllUsers() {
        return userMapper.toUserDtoSet(userRepository.findAll());
    }

    @Override
    public UserDto getUserById(Long id) {
        return userMapper.toUserDto(Objects.requireNonNull(StreamSupport.stream(userRepository.findAll().spliterator(), false).
                filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null)));
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public String login(LoginRequest request) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("user doesn't exists"));
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getId().toString(), request.getPassword()));

        user.setLastLogin(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);

        return jwtTokenProvider.createToken(user.getId());
    }

    @Override
    public UserDto register(RegisterRequest request) throws EmailAlreadyExistsException, PhoneNumberAlreadyExistsException, RegistrationException {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("invalid registration request: email already exists");
        }

        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new PhoneNumberAlreadyExistsException("invalid registration request: phone number already exists");
        }

        if (!EnumUtils.isValidEnum(Status.class, request.getStatus())) {
            throw new RegistrationException("invalid registration request: status field is invalid");
        }

        if (!EnumUtils.isValidEnum(Role.class, request.getRole())) {
            throw new RegistrationException("invalid registration request: role field is invalid");
        }

        if (request.getPhoneNumber() == null) {
            throw new RegistrationException("invalid registration request: phone number field is invalid");
        }

        User newUser = getUserFromRegisterRequest(request);
        userRepository.save(newUser);
        return userMapper.toUserDto(newUser);
    }

    public User getUserFromRegisterRequest(RegisterRequest request) {
        return new User(
                request.getFirstName(), request.getSecondName(),
                request.getEmail(), request.getPhoneNumber(),
                passwordEncoder.encode(request.getPassword()),
                Status.valueOf(request.getStatus()),
                Role.valueOf(request.getRole())
        );
    }

    @Override
    public void updateProfile(RegisterRequest request) throws UserIsNotLoggedInException {
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
    public void updateProfilePhoto(byte[] bytes) throws IOException, UserIsNotLoggedInException, InvalidImageSizeException {
        User userModel = authContextHandler.getLoggedInUser();

        if (userModel.getProfilePhoto() != null) {
            storage.delete(BlobId.of(bucketName, userModel.getProfilePhoto()));
        }

        BlobId blobId = BlobId.of(bucketName, getUniqueProfilePhotoPath(userModel));
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        storage.create(blobInfo, imageService.scaleImage(bytes));

        userModel.setProfilePhoto(getUniqueProfilePhotoPath(userModel));
        userRepository.save(userModel);
    }

    @Override
    public void deleteProfilePhoto() throws UserIsNotLoggedInException {
        User userModel = authContextHandler.getLoggedInUser();
        if (userModel.getProfilePhoto() != null) {
            storage.delete(BlobId.of(bucketName, userModel.getProfilePhoto()));
            userModel.setProfilePhoto(null);
            userRepository.save(userModel);
        }
    }

    @Override
    public URL getProfilePhoto() throws UserIsNotLoggedInException {
        User userModel = authContextHandler.getLoggedInUser();
        return googleStorageSignedUrlGenerator.generate(bucketName, getUniqueProfilePhotoPath(userModel));
    }

    private String getUniqueProfilePhotoPath(User user) {
        return profilePhotosPath + user.getId() + "." + defaultFormat;
    }
}
