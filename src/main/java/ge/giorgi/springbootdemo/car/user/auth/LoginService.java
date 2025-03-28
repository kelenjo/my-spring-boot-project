package ge.giorgi.springbootdemo.car.user.auth;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import ge.giorgi.springbootdemo.car.error.InvalidLoginException;
import ge.giorgi.springbootdemo.car.user.UserService;
import ge.giorgi.springbootdemo.car.user.persistence.AppUser;
import ge.giorgi.springbootdemo.car.user.persistence.Role;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoginService {

    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    @Value("${jwt.secret-key}")
    private String secretKey;

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest loginRequest) {
        logger.info("User attempting to log in: {}", loginRequest.getUsername());
        AppUser appUser = userService.findByUsername(loginRequest.getUsername());

        if (!passwordEncoder.matches(loginRequest.getPassword(), appUser.getPassword())) {
            logger.warn("Invalid login attempt for user: {}", loginRequest.getUsername());
            throw new InvalidLoginException("invalid-login");
        }

        LoginResponse response = generateLoginResponse(appUser);
        logger.info("User successfully logged in: {}", loginRequest.getUsername());
        return response;
    }

    private LoginResponse generateLoginResponse(AppUser user) {
        try {
            Instant expirationTime = Instant.now().plus(1, ChronoUnit.HOURS); // 1 hour expiration

            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .subject(user.getUsername())
                    .claim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
                    .issuer("carsapp.ge")
                    .expirationTime(Date.from(expirationTime)) // Convert Instant to Date
                    .build();

            JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
            SignedJWT signedJWT = new SignedJWT(header, claims);
            signedJWT.sign(new MACSigner(secretKey.getBytes()));

            return new LoginResponse(signedJWT.serialize());

        } catch (Exception e) {
            logger.error("Failed to generate JWT token", e);
            throw new InvalidLoginException("Failed to generate token");
        }
    }

    public void logout(String token) {
        logger.info("User logging out with token: {}", token);

        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            Instant expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime().toInstant();

            if (expirationTime != null) {
                // Calculate the remaining time until expiration in milliseconds
                long ttl = expirationTime.toEpochMilli() - Instant.now().toEpochMilli();

                logger.info("Token has been invalidated and will expire at the token's expiration time.");
            } else {
                logger.warn("Token does not have an expiration time.");
            }
        } catch (Exception e) {
            logger.error("Error during token parsing: {}", e.getMessage());
        }
    }

}
