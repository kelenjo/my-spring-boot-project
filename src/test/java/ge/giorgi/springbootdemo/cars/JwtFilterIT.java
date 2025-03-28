package ge.giorgi.springbootdemo.cars;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class JwtFilterIT {

    @Autowired
    private MockMvc mockMvc;

    @Value("${jwt.secret-key}")
    private String secretKey;

    private String validToken;
    private String expiredToken;

    @BeforeEach
    void setUp() throws Exception {
        validToken = generateJwtToken(Instant.now().plus(1, ChronoUnit.HOURS)); // Valid for 1 hour
        expiredToken = generateJwtToken(Instant.now().minus(1, ChronoUnit.HOURS)); // Already expired
    }

    private String generateJwtToken(Instant expiration) throws Exception {
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject("admin")
                .claim("roles", Collections.singletonList("ADMIN"))
                .issuer("carsapp.ge")
                .expirationTime(Date.from(expiration))
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claims);
        signedJWT.sign(new MACSigner(secretKey.getBytes()));
        return signedJWT.serialize();
    }

    @Test
    void shouldAuthenticateUserWithValidJwt() throws Exception {
        mockMvc.perform(get("/users/account")
                        .header("Authorization", "Bearer " + validToken))
                        .andExpect(status().isOk());
   }

    @Test
    void shouldRejectRequestWithInvalidJwt() throws Exception {
        mockMvc.perform(get("/users")
                        .header("Authorization", "Bearer invalid_token"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldRejectExpiredJwt() throws Exception {
        mockMvc.perform(get("/users")
                        .header("Authorization", "Bearer " + expiredToken))
                .andExpect(status().isForbidden());
    }
}
