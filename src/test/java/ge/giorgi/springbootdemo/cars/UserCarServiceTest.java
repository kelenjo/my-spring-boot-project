package ge.giorgi.springbootdemo.cars;

import ge.giorgi.springbootdemo.car.CarService;
import ge.giorgi.springbootdemo.car.dto.PaginationRequest;
import ge.giorgi.springbootdemo.car.error.NotFoundException;
import ge.giorgi.springbootdemo.car.models.CarDTO;
import ge.giorgi.springbootdemo.car.persistence.Car;
import ge.giorgi.springbootdemo.car.user.OwnedCarsDTO;
import ge.giorgi.springbootdemo.car.user.UserCarService;
import ge.giorgi.springbootdemo.car.user.persistence.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserCarServiceTest {

    @Mock
    private UserCarRepository userCarRepository;

    @Mock
    private CarService carService;

    @InjectMocks
    private UserCarService userCarService;

    private AppUser user;
    private Car car;
    private UserCar userCar;

    @BeforeEach
    void setUp() {
        user = new AppUser();
        user.setId(1L);

        car = new Car();
        car.setId(1L);
        car.setPriceInCents(100000);

        userCar = new UserCar();
        userCar.setId(1L);
        userCar.setUser(user);
        userCar.setCar(car);
        userCar.setPurchaseTime(Instant.now());
        userCar.setBoughtFor(car.getPriceInCents());
        userCar.setSellingFor(null);
    }

    @Test
    void addCar_shouldSaveUserCar() {
        userCarService.addCar(user, car);
        verify(userCarRepository, times(1)).save(any(UserCar.class));
    }

    @Test
    void getUserCars_shouldReturnUserCars() {
        PaginationRequest request = new PaginationRequest(0, 10);
        Page<OwnedCarsDTO> expectedPage = new PageImpl<>(List.of());
        when(userCarRepository.findByUser_Id(any(PageRequest.class), eq(user.getId()))).thenReturn(expectedPage);

        Page<OwnedCarsDTO> result = userCarService.getUserCars(request, user.getId());
        assertEquals(expectedPage, result);
    }



    @Test
    void getAllUserCars_shouldReturnAllUserCars() {
        PaginationRequest request = new PaginationRequest(0, 10);
        Page<OwnedCarsDTO> expectedPage = new PageImpl<>(List.of());
        when(userCarRepository.findAllCars(any(PageRequest.class))).thenReturn(expectedPage);

        Page<OwnedCarsDTO> result = userCarService.getAllUserCars(request);
        assertEquals(expectedPage, result);
    }

    @Test
    void findById_shouldReturnUserCar() {
        when(userCarRepository.findById(userCar.getId())).thenReturn(Optional.of(userCar));
        UserCar result = userCarService.findById(userCar.getId());
        assertEquals(userCar, result);
    }

    @Test
    void findById_shouldThrowNotFoundException() {
        when(userCarRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userCarService.findById(999L));
    }

    @Test
    void save_shouldCallRepositorySave() {
        userCarService.save(userCar);
        verify(userCarRepository, times(1)).save(userCar);
    }

    @Test
    void getAllCompanySellingCars_shouldReturnPagedCars() {
        PaginationRequest request = new PaginationRequest(0, 10);
        Page<CarDTO> expectedPage = new PageImpl<>(List.of());
        when(carService.getCars(any(PaginationRequest.class))).thenReturn(expectedPage);

        Page<CarDTO> result = userCarService.getAllCompanySellingCars(request);
        assertEquals(expectedPage, result);
    }

    @Test
    void getAllUserSellingCars_shouldReturnPagedSellingCars() {
        PaginationRequest request = new PaginationRequest(0, 10);
        Page<OwnedCarsDTO> expectedPage = new PageImpl<>(List.of());
        when(userCarRepository.findAllSellingCars(any(PageRequest.class))).thenReturn(expectedPage);

        Page<OwnedCarsDTO> result = userCarService.getAllUserSellingCars(request);
        assertEquals(expectedPage, result);
    }
}
