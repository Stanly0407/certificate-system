package com.epam.esm.services.service;

import com.epam.esm.entities.RefreshToken;
import com.epam.esm.entities.User;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.services.dto.JwtResponse;
import com.epam.esm.services.exceptions.ResourceNotFoundException;
import com.epam.esm.services.exceptions.UnprocessableEntityException;
import com.epam.esm.services.requests.LoginRequest;
import com.epam.esm.services.requests.SignupRequest;
import com.epam.esm.services.service.security.UserDetailsImpl;
import com.epam.esm.services.service.utils.JwtGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtGenerator jwtGenerator;
    private final PasswordEncoder encoder;
    private final RefreshTokenService refreshTokenService;

    public UserServiceImpl(UserRepository userRepository, AuthenticationManager authenticationManager,
                           JwtGenerator jwtGenerator, PasswordEncoder encoder, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtGenerator = jwtGenerator;
        this.encoder = encoder;
        this.refreshTokenService = refreshTokenService;
    }

    public User getById(Long id) throws ResourceNotFoundException {
        Optional<User> userOptional = userRepository.getById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new ResourceNotFoundException(id);
        }
    }

    public List<User> getAllUsers(int pageNumber, int pageSize) throws ResourceNotFoundException {
        long pageQuantity = getUsersPaginationInfo(pageNumber, pageSize);
        if (pageNumber <= pageQuantity) {
            return userRepository.getAllUsers(pageNumber, pageSize);
        } else {
            throw new ResourceNotFoundException(pageNumber);
        }
    }

    public long getUsersPaginationInfo(int pageNumber, int pageSize) {
        long countResult = userRepository.getUsersCommonQuantity();
        if ((countResult % pageSize) != 0) {
            return (countResult / pageSize) + 1;
        } else {
            return (countResult / pageSize);
        }
    }

    public Long saveNewUser(SignupRequest signupRequest) throws UnprocessableEntityException {
        String login = signupRequest.getLogin();
        if (userRepository.existsByLogin(login)) {
            LOGGER.info("Error: login - " + login + " - is already taken!");
            throw new UnprocessableEntityException(login);
        }
        String password = encoder.encode(signupRequest.getPassword());
        String name = signupRequest.getName();
        String lastname = signupRequest.getLastname();

        // Create new user's account
        User user = User.builder().login(login).name(name).lastname(lastname).password(password).build();
        Long id = userRepository.save(user);
        LOGGER.info("User with id = " + id + " registered successfully!");
        userRepository.saveRoleUser(id);
        return id;
    }

    public JwtResponse login(LoginRequest form) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(form.getLogin(), form.getPassword());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtGenerator.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long id = userDetails.getId();
        String login = userDetails.getUsername();

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        LOGGER.info("User with id = " + id + ", login = " + login + " login successfully!");
        return JwtResponse.builder()
                .login(login)
                .accessToken(jwt)
                .refreshToken(refreshToken.getToken())
                .build();
    }

}
