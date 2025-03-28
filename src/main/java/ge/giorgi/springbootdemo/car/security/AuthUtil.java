package ge.giorgi.springbootdemo.car.security;

import ge.giorgi.springbootdemo.car.user.persistence.AppUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {

    public static final String ADMIN = "hasAuthority('ROLE_ADMIN')";
    public static final String USER_OR_ADMIN = "hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')";

    public static String getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            return authentication.getName();
        }
        throw new RuntimeException("Unauthenticated access");
    }

    public static boolean hasAdminRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }

}