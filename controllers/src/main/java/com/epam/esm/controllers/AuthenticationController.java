package com.epam.esm.controllers;

import com.epam.esm.services.dto.JwtResponse;
import com.epam.esm.services.dto.TokenRefreshResponse;
import com.epam.esm.services.exceptions.BadRequestException;
import com.epam.esm.services.forms.LoginForm;
import com.epam.esm.services.forms.SignupForm;
import com.epam.esm.services.forms.TokenRefreshRequest;
import com.epam.esm.services.service.RefreshTokenService;
import com.epam.esm.services.service.UserService;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * A class {@code AuthenticationController} as request handler defines method which accepts
 * user authentication requests and performs interactions on the data model objects by using service layer.
 *
 * @author Sviatlana Shelestava
 * @since 1.0
 */
@RestController
@RequestMapping("public")
@Validated
public class AuthenticationController {

    private final UserService userService;
    private final LinkBuilder linkBuilder;
    private final RefreshTokenService refreshTokenService;

    public AuthenticationController(UserService userService, LinkBuilder linkBuilder, RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.linkBuilder = linkBuilder;
        this.refreshTokenService = refreshTokenService;
    }

    /**
     * Checks if a user exists with the specified login and password and,
     * if successful, issues a token to access application resources;
     *
     * @param loginForm contains base information of the user: login and password;
     * @return ResponseEntity representing the whole HTTP response: status code 200 and headers
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginForm loginForm) {
        JwtResponse jwtResponse = userService.login(loginForm);
        return ResponseEntity.ok(jwtResponse);
    }

    /**
     * Creates a new user with role USER;
     *
     * @param signupForm contains base information of user to be created;
     * @return ResponseEntity representing the whole HTTP response: status code 201 and headers
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signupNewUser(@RequestBody @Valid SignupForm signupForm) throws BadRequestException {
        long newUserId = userService.saveNewUser(signupForm);
        Link newUserLink = linkBuilder.getSelfLink(newUserId, AuthenticationController.class);
        return ResponseEntity.created(newUserLink.toUri()).build();
    }

    /**
     * Creates a new RefreshToken for logged in user;
     *
     * @param tokenRefreshRequest is a users's RefreshToken;
     * @return ResponseEntity representing the whole HTTP response: status code 200 and headers
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody @Valid TokenRefreshRequest tokenRefreshRequest) {
        TokenRefreshResponse tokenRefreshResponse = refreshTokenService.refreshToken(tokenRefreshRequest);
        return ResponseEntity.ok(tokenRefreshResponse);
    }

}
