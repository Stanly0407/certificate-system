package com.epam.esm.services.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TokenRefreshResponse {

    private String accessToken;
    private String refreshToken;
    private final String tokenType = "Bearer";

}