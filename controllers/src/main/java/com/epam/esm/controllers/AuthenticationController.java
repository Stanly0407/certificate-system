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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginForm form) {
        JwtResponse jwtResponse = userService.login(form);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signupNewUser(@RequestBody @Valid SignupForm signupForm) throws BadRequestException {
        long newUserId = userService.saveNewUser(signupForm);
        Link newUserLink = linkBuilder.getSelfLink(newUserId, AuthenticationController.class);
        return ResponseEntity.created(newUserLink.toUri()).build();
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        TokenRefreshResponse tokenRefreshResponse = refreshTokenService.refreshToken(request);
        return ResponseEntity.ok(tokenRefreshResponse);
    }

}
