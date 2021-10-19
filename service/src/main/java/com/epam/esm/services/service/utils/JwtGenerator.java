package com.epam.esm.services.service.utils;

import com.epam.esm.services.exceptions.BadRequestException;
import com.epam.esm.services.service.security.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtGenerator {

    private static final Logger LOGGER = LogManager.getLogger(JwtGenerator.class);

    @Value("${epam.app.jwtSecret}")
    private String jwtSecret;

    @Value("${epam.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        String subject = userPrincipal.getUsername(); // login
        return generateTokenFromLogin(subject);
    }

    public String generateTokenFromLogin(String login) {
        Date now = new Date();
        Date expiresJwt = new Date((new Date()).getTime() + jwtExpirationMs);
        return Jwts.builder()
                .setSubject(login)
                .setIssuedAt(now)
                .setExpiration(expiresJwt)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getLoginFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException exception) {
            LOGGER.info("Invalid JWT signature: {}", exception.getMessage());
        } catch (MalformedJwtException exception) {
            LOGGER.info("Invalid JWT token: {}", exception.getMessage());
        } catch (ExpiredJwtException exception) {
            LOGGER.info("JWT token is expired: {}", exception.getMessage());
        } catch (UnsupportedJwtException exception) {
            LOGGER.info("JWT token is unsupported: {}", exception.getMessage());
        } catch (IllegalArgumentException exception) {
            LOGGER.info("JWT claims string is empty: {}", exception.getMessage());
        }
        return false;
    }

}
