package com.epam.esm.services.service;

import com.epam.esm.entities.RefreshToken;
import com.epam.esm.repository.RefreshTokenRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.services.dto.TokenRefreshResponse;
import com.epam.esm.services.exceptions.ExceptionMessageType;
import com.epam.esm.services.exceptions.TokenRefreshException;
import com.epam.esm.services.forms.TokenRefreshRequest;
import com.epam.esm.services.service.utils.JwtGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static com.epam.esm.services.exceptions.ExceptionMessageType.UNKNOWN_REFRESH_TOKEN;

@Service
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${epam.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtGenerator jwtGenerator;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository,
                                   JwtGenerator jwtGenerator) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.jwtGenerator = jwtGenerator;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userRepository.getById(userId).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(ExceptionMessageType.EXPIRED);
        }
        return token;
    }

    public TokenRefreshResponse refreshToken(TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        return findByToken(requestRefreshToken)
                .map(this::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtGenerator.generateTokenFromLogin(user.getLogin());
                    return new TokenRefreshResponse(token, requestRefreshToken);
                })
                .orElseThrow(() -> new TokenRefreshException(UNKNOWN_REFRESH_TOKEN));
    }

}
