package ge.giorgi.springbootdemo.cars;

import ge.giorgi.springbootdemo.car.dto.PaginationRequest;
import ge.giorgi.springbootdemo.car.error.AccessDeniedException;
import ge.giorgi.springbootdemo.car.error.NotFoundException;
import ge.giorgi.springbootdemo.car.user.RoleService;
import ge.giorgi.springbootdemo.car.user.UserService;
import ge.giorgi.springbootdemo.car.user.model.UserDTO;
import ge.giorgi.springbootdemo.car.user.model.UserRequest;
import ge.giorgi.springbootdemo.car.user.persistence.AppUser;
import ge.giorgi.springbootdemo.car.user.persistence.AppUserRepository;
import ge.giorgi.springbootdemo.car.user.persistence.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private UserService userService;

    private AppUser user;

    @BeforeEach
    void setUp() {
        user = new AppUser();
        user.setId(1L);
        user.setUsername("testUser");
        user.setPassword("encodedPassword");
        user.setBalanceInCents(5000);
        user.setRoles(Set.of(new Role()));
    }

    @Test
    void testCreateUser() {
        UserRequest userRequest = new UserRequest("newUser", "password123", 10000, Set.of(1L));
        when(passwordEncoder.encode(userRequest.getPassword())).thenReturn("encodedPassword");
        when(roleService.getRole(1L)).thenReturn(new Role());
        when(appUserRepository.save(any(AppUser.class))).thenAnswer(invocation -> {
            AppUser userArg = invocation.getArgument(0);
            userArg.setId(1L); // Simulate DB assigning an ID
            return userArg;
        });
        long userId = userService.createUser(userRequest);

        assertEquals(1L, userId);
        verify(appUserRepository, times(1)).save(any(AppUser.class));
    }

    @Test
    void testFindByUsername_UserExists() {
        when(appUserRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        AppUser foundUser = userService.findByUsername("testUser");
        assertEquals("testUser", foundUser.getUsername());
    }

    @Test
    void testFindByUsername_UserNotFound() {
        when(appUserRepository.findByUsername("unknownUser")).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.findByUsername("unknownUser"));
    }

    @Test
    void testFindById_UserExists() {
        when(appUserRepository.findById(1L)).thenReturn(Optional.of(user));
        AppUser foundUser = userService.findById(1L);
        assertEquals(1L, foundUser.getId());
    }

    @Test
    void testFindById_UserNotFound() {
        when(appUserRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.findById(2L));
    }

    @Test
    void testGetUsers() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<AppUser> userPage = new PageImpl<>(List.of(user));
        when(appUserRepository.findAll(pageRequest)).thenReturn(userPage);

        Page<UserDTO> result = userService.getUsers(new PaginationRequest(0, 10));
        assertEquals(1, result.getContent().size());
    }
}
