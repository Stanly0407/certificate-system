package com.epam.esm.services.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class JwtResponse {

    private String token;

    private final String type = "Bearer"; // ru = токен предъявителя

    private Long id;

    private String login;

    private List<String> roles;

}
