package com.github.daniilandco.vehicle_sales_project.service.impl;

import com.github.daniilandco.vehicle_sales_project.dto.model.UserDTO;
import com.github.daniilandco.vehicle_sales_project.dto.request.LoginRequestDTO;
import com.github.daniilandco.vehicle_sales_project.dto.request.RegisterRequestDTO;
import com.github.daniilandco.vehicle_sales_project.dto.response.SuccessLoginResponse;
import com.github.daniilandco.vehicle_sales_project.exception.auth.*;
import com.github.daniilandco.vehicle_sales_project.exception.image.InvalidImageSizeException;
import com.github.daniilandco.vehicle_sales_project.exception.token.InvalidTokenException;
import com.github.daniilandco.vehicle_sales_project.mapper.UserMapper;
import com.github.daniilandco.vehicle_sales_project.model.user.Role;
import com.github.daniilandco.vehicle_sales_project.model.user.Status;
import com.github.daniilandco.vehicle_sales_project.model.user.User;
import com.github.daniilandco.vehicle_sales_project.repository.token.TokenRepository;
import com.github.daniilandco.vehicle_sales_project.repository.user.UserRepository;
import com.github.daniilandco.vehicle_sales_project.security.AuthContextHandler;
import com.github.daniilandco.vehicle_sales_project.security.jwt.JwtTokenProvider;
import com.github.daniilandco.vehicle_sales_project.security.jwt.TokenResponse;
import com.github.daniilandco.vehicle_sales_project.service.ImageService;
import com.github.daniilandco.vehicle_sales_project.service.MailService;
import com.github.daniilandco.vehicle_sales_project.service.UserService;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
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
    private final MailService mailService;
    private final TokenRepository tokenRepository;

    @Value("${cloud.bucketname}")
    private String bucketName;
    @Value("${cloud.default.photo.format}")
    private String defaultFormat;
    @Value("${cloud.subdirectory.profile}")
    private String profilePhotosPath;
    @Value("${spring.server.hostname}")
    private String hostname;

    private static final String ACTIVATED_ACCOUNT_CODE = "ACTIVATED";

    public UserServiceImplementation(PasswordEncoder passwordEncoder, UserRepository userRepository, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, Storage storage, UserMapper userMapper, GoogleStorageSignedUrlGenerator googleStorageSignedUrlGenerator, AuthContextHandler authContextHandler, ImageService imageService, MailService mailService, TokenRepository tokenRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.storage = storage;
        this.userMapper = userMapper;
        this.googleStorageSignedUrlGenerator = googleStorageSignedUrlGenerator;
        this.authContextHandler = authContextHandler;
        this.imageService = imageService;
        this.mailService = mailService;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public Iterable<UserDTO> getAllUsers() {
        return userMapper.toUserDtoList(userRepository.findAll());
    }

    @Override
    public UserDTO getUserById(Long id) {
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
    public SuccessLoginResponse login(LoginRequestDTO request) throws UsernameNotFoundException, UserIsNotLoggedInException {
        User user = userRepository.findByEmailAndActivationCode(request.email(), ACTIVATED_ACCOUNT_CODE)
                .orElseThrow(() -> new UsernameNotFoundException("user does not exist"));
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), request.password()));

        user.setLastLogin(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);

        TokenResponse tokenResponse = jwtTokenProvider.createToken(user.getEmail());
        jwtTokenProvider.saveToken(user.getId(), tokenResponse.getRefreshToken());

        return new SuccessLoginResponse(userMapper.toUserDto(user), tokenResponse);
    }

    @Override
    @Transactional
    public void logout(String refreshToken, HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
        jwtTokenProvider.removeToken(refreshToken);
    }

    @Override
    public UserDTO register(RegisterRequestDTO request) throws EmailAlreadyExistsException, PhoneNumberAlreadyExistsException, RegistrationException, UserIsNotLoggedInException {
        if (StringUtils.isEmpty(request.email()) || userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("invalid registration request: email already exists");
        }

        if (userRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new PhoneNumberAlreadyExistsException("invalid registration request: phone number already exists");
        }

        if (!EnumUtils.isValidEnum(Status.class, request.status().name())) {
            throw new RegistrationException("invalid registration request: status field is invalid");
        }

        if (!EnumUtils.isValidEnum(Role.class, request.role().name())) {
            throw new RegistrationException("invalid registration request: role field is invalid");
        }

        if (request.phoneNumber() == null) {
            throw new RegistrationException("invalid registration request: phone number field is invalid");
        }

        User newUser = getUserFromRegisterRequest(request);
        newUser.setActivationCode(UUID.randomUUID().toString());

        String message = String.format("Hello, %s!\n" +
                        "Welcome to Vehicle Sales Service. For account activation, please visit the link:" +
                        "%s/auth/activate/%s",
                newUser.getFirstName(),
                hostname,
                newUser.getActivationCode());

        mailService.send(request.email(), "Activation code", message);

        userRepository.save(newUser);

        return userMapper.toUserDto(newUser);
    }

    @Override
    public SuccessLoginResponse refresh(String refreshToken) throws UserIsNotLoggedInException, InvalidTokenException {
        if (jwtTokenProvider.validateRefreshToken(refreshToken)
                && tokenRepository.existsByRefreshToken(refreshToken)) {

            User user = authContextHandler.getLoggedInUser();

            TokenResponse tokenResponse = jwtTokenProvider.createToken(user.getEmail());
            jwtTokenProvider.saveToken(user.getId(), tokenResponse.getRefreshToken());

            return new SuccessLoginResponse(userMapper.toUserDto(user), tokenResponse);
        } else {
            throw new InvalidTokenException("refresh token is invalid");
        }
    }

    @Override
    public void activateUser(String code) throws UnsuccessfulActivationException {
        Optional<User> user = userRepository.findByActivationCode(code);
        if (user.isEmpty()) {
            throw new UnsuccessfulActivationException("no user with such activation code");
        }
        User userModel = user.get();
        userModel.setActivationCode(ACTIVATED_ACCOUNT_CODE);
        userRepository.save(userModel);
    }

    public User getUserFromRegisterRequest(RegisterRequestDTO request) {
        return new User()
                .setFirstName(request.firstName())
                .setSecondName(request.secondName())
                .setEmail(request.email())
                .setPhoneNumber(request.phoneNumber())
                .setPassword(passwordEncoder.encode(request.password()))
                .setLocation(request.location())
                .setStatus(request.status())
                .setRole(request.role())
                .setProfilePhoto(request.profilePhoto());
    }

    @Override
    public void updateProfile(RegisterRequestDTO request) throws UserIsNotLoggedInException {
        User userModel = authContextHandler.getLoggedInUser();

        if (request.firstName() != null) {
            userModel.setFirstName(request.firstName());
        }
        if (request.secondName() != null) {
            userModel.setSecondName(request.secondName());
        }
        if (request.phoneNumber() != null) {
            userModel.setPhoneNumber(request.phoneNumber());
        }
        if (request.password() != null) {
            userModel.setPassword(passwordEncoder.encode(request.password()));
        }
        if (request.location() != null) {
            userModel.setLocation(request.location());
        }
        if (request.profilePhoto() != null) {
            userModel.setProfilePhoto(request.profilePhoto());
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
