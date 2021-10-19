package com.epam.esm.services.service;

import com.epam.esm.entities.RefreshToken;
import com.epam.esm.services.dto.TokenRefreshResponse;

/**
 * An interface {@code RefreshTokenService} defines the service layer for a RefreshToken entity with business logic
 * methods (fetching data, deleting, etc.) that access the data access layer.
 *
 * @author Sviatlana Shelestava
 * @since 3.0
 */
public interface RefreshTokenService {

    /**
     * Creates a new RefreshToken for logged in user;
     *
     * @param userId is a unique field of user in database;
     * @return RefreshToken of just logged in user;
     */
    RefreshToken createRefreshToken(Long userId);

    /**
     * Creates a new RefreshToken for logged in user;
     *
     * @param refreshToken is a users's RefreshToken;
     * @return TokenRefreshResponse with generated new valid user's JWT token;
     */
    TokenRefreshResponse refreshToken(String refreshToken);

}
