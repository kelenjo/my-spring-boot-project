package ge.giorgi.springbootdemo.car.security;

import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        logger.info("Entered JwtFilter...");
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.debug("No JWT token found in request header.");
            chain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.substring(7);
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(secretKey.getBytes());
            if (!signedJWT.verify(verifier)) {
                logger.warn("JWT signature verification failed.");
                chain.doFilter(request, response);
                return;
            }

            Instant expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime().toInstant();
            if (expirationTime == null || expirationTime.isBefore(Instant.now())) {
                logger.warn("JWT token is expired.");
                chain.doFilter(request, response);
                return;
            }

            String username = signedJWT.getJWTClaimsSet().getSubject();
            List<String> roles = signedJWT.getJWTClaimsSet().getStringListClaim("roles");

            // Set authentication context for the user
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    username, null, roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList()));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            logger.info("Successfully authenticated user: {}", username);
        } catch (Exception e) {
            logger.error("Error during JWT extraction: {}", e.getMessage());
        }

        chain.doFilter(request, response);
    }
}
