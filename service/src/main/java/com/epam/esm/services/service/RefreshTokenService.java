package com.epam.esm.services.service;

import com.epam.esm.entities.RefreshToken;
import com.epam.esm.services.dto.TokenRefreshResponse;
import com.epam.esm.services.forms.TokenRefreshRequest;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(Long userId);

    TokenRefreshResponse refreshToken(TokenRefreshRequest request);

}
