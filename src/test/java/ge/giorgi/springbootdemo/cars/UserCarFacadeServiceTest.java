package ge.giorgi.springbootdemo.cars;

import ge.giorgi.springbootdemo.car.CarService;
import ge.giorgi.springbootdemo.car.dto.PaginationRequest;
import ge.giorgi.springbootdemo.car.dto.PriceRequest;
import ge.giorgi.springbootdemo.car.error.AccessDeniedException;
import ge.giorgi.springbootdemo.car.error.InvalidPurchasingException;
import ge.giorgi.springbootdemo.car.error.NotEnoughMoneyException;
import ge.giorgi.springbootdemo.car.error.NotFoundException;
import ge.giorgi.springbootdemo.car.models.CarDTO;
import ge.giorgi.springbootdemo.car.models.EngineDTO;
import ge.giorgi.springbootdemo.car.persistence.Car;
import ge.giorgi.springbootdemo.car.user.OwnedCarsDTO;
import ge.giorgi.springbootdemo.car.user.UserCarFacadeService;
import ge.giorgi.springbootdemo.car.user.UserCarService;
import ge.giorgi.springbootdemo.car.user.UserService;
import ge.giorgi.springbootdemo.car.user.persistence.AppUser;
import ge.giorgi.springbootdemo.car.user.persistence.UserCar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserCarFacadeServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private UserCarService userCarService;

    @Mock
    private CarService carService;

    @InjectMocks
    private UserCarFacadeService userCarFacadeService;

    private OwnedCarsDTO ownedCarsDTO;

    // Mock security context setup
    @BeforeEach
    public void setupSecurityContext() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("admin"); // Simulate the authenticated user as "admin"
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext); // Set the mock SecurityContext
    }

    @Test
    public void testGetUserCars() {
        AppUser user = new AppUser();
        user.setId(1000L);

        PaginationRequest paginationRequest = new PaginationRequest(0, 10);
        Page<OwnedCarsDTO> page = new PageImpl<>(List.of());
        when(userService.findByUsername("admin")).thenReturn(user);
        when(userCarService.getUserCars(paginationRequest, user.getId())).thenReturn(page);

        Page<OwnedCarsDTO> result = userCarFacadeService.getUserCars(paginationRequest, Optional.of(1000L));

        assertNotNull(result);
        verify(userCarService).getUserCars(paginationRequest, user.getId());
    }

    @Test
    public void testBuyCarFromCompany() {
        AppUser user = new AppUser();
        user.setId(1000L);
        user.setBalanceInCents(5000);

        Car car = new Car();
        car.setId(1007);

        when(userService.findByUsername("admin")).thenReturn(user);
        when(carService.findCar(car.getId())).thenReturn(car);

        userCarFacadeService.buyCarFromCompany(car.getId());

        assertEquals(5000, user.getBalanceInCents());
        verify(userCarService).addCar(user, car);
        verify(userService).save(user);
    }

    @Test
    public void testSellCar() {
        AppUser user = new AppUser();
        user.setId(1000L);

        UserCar userCar = new UserCar();
        userCar.setId(1001L);
        userCar.setUser(user);

        when(userService.findByUsername("admin")).thenReturn(user);
        when(userCarService.findById(userCar.getId())).thenReturn(userCar);

        PriceRequest priceRequest = new PriceRequest(3000);

        userCarFacadeService.sellCar(userCar.getId(), priceRequest);

        assertEquals(3000, userCar.getSellingFor());
        verify(userCarService).save(userCar);
    }

    @Test
    public void testRemoveCarFromSelling() {
        AppUser user = new AppUser();
        user.setId(1000L);

        UserCar userCar = new UserCar();
        userCar.setId(1001L);
        userCar.setUser(user);
        userCar.setSellingFor(10L);

        when(userService.findByUsername("admin")).thenReturn(user);
        when(userCarService.findById(userCar.getId())).thenReturn(userCar);

        userCarFacadeService.removeCarFromSelling(userCar.getId());

        assertNull(userCar.getSellingFor());
        verify(userCarService, times(1)).save(userCar);
    }

    @Test
    public void testAccessDeniedOnSellingCarNotOwned() {
        AppUser user = new AppUser();
        AppUser otherUser = new AppUser();
        user.setId(1000L);
        otherUser.setId(1001L);

        UserCar userCar = new UserCar();
        userCar.setId(1000L);
        userCar.setUser(otherUser);

        when(userService.findByUsername(anyString())).thenReturn(user);
        when(userCarService.findById(userCar.getId())).thenReturn(userCar);

        assertThrows(AccessDeniedException.class, () -> userCarFacadeService.sellCar(userCar.getId(), new PriceRequest(3000)));
    }

    @Test
    public void testBuyCarFromUser_AlreadyOwned() {
        AppUser user = new AppUser();
        user.setId(1000L);

        UserCar userCar = new UserCar();
        userCar.setId(1001L);
        userCar.setUser(user);

        when(userService.findByUsername("admin")).thenReturn(user);
        when(userCarService.findById(userCar.getId())).thenReturn(userCar);

        assertThrows(InvalidPurchasingException.class, () -> userCarFacadeService.buyCarFromUser(userCar.getId()));
    }

    @Test
    public void testBuyCarFromUser_NotForSale() {
        AppUser user = new AppUser();
        AppUser otherUser = new AppUser();
        user.setId(1000L);
        otherUser.setId(1001L);

        UserCar userCar = new UserCar();
        userCar.setId(1001L);
        userCar.setUser(otherUser);
        userCar.setSellingFor(null);

        when(userService.findByUsername("admin")).thenReturn(user);
        when(userCarService.findById(userCar.getId())).thenReturn(userCar);

        assertThrows(NotFoundException.class, () -> userCarFacadeService.buyCarFromUser(userCar.getId()));
    }

    @Test
    public void testBuyCarFromUser_NotEnoughMoney() {
        AppUser user = new AppUser();
        AppUser otherUser = new AppUser();
        user.setId(1000L);
        user.setBalanceInCents(1);
        otherUser.setId(1001L);

        Car car = new Car();
        car.setId(1L);

        UserCar userCar = new UserCar();
        userCar.setId(1001L);
        userCar.setUser(otherUser);
        userCar.setSellingFor(10000L);
        userCar.setCar(car);

        when(userService.findByUsername("admin")).thenReturn(user);
        when(userCarService.findById(userCar.getId())).thenReturn(userCar);

        assertThrows(NotEnoughMoneyException.class, () -> userCarFacadeService.buyCarFromUser(userCar.getId()));
    }
}
