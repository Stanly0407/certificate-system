package com.epam.esm.repository;

import com.epam.esm.entities.RefreshToken;

import java.util.Optional;

/**
 * An interface {@code RefreshTokenRepository} defines the methods for retrieving refresh token data from the database;
 *
 * @author Sviatlana Shelestava
 * @since 1.0
 */
public interface RefreshTokenRepository {

    Optional<RefreshToken> findById(Long id);

    Optional<RefreshToken> findByToken(String token);

    RefreshToken save(RefreshToken refreshToken);

    void delete(Long id);

    void deleteByUserId(Long userId);

}
