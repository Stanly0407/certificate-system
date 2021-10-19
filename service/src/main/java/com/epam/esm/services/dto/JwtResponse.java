package com.epam.esm.services.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtResponse {

    private String accessToken;
    private final String type = "Bearer";
    private String refreshToken;
    private String login;

}
