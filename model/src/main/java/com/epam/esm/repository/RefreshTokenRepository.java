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

    /**
     * Executes an SQL <code>SELECT</code> statement, which searches for RefreshToken entity by token value;
     *
     * @param token is a token value of RefreshToken in database;
     * @return an <code>Optional</code> contains the RefreshToken with matching token value
     * or <code>Optional</code> contain a null value;
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * Executes the SQL <code>INSERT</code> statement, which creates new user's refreshToken in database;
     *
     * @param refreshToken is a refreshToken to be created;
     * @return a just created user's <code>RefreshToken</code>;
     */
    RefreshToken save(RefreshToken refreshToken);

    /**
     * Executes the SQL <code>DELETE</code> statement, which deletes this user's refreshToken from database;
     *
     * @param refreshToken is a refreshToken of the user to be deleted from a database;
     */
    void delete(RefreshToken refreshToken);

}
