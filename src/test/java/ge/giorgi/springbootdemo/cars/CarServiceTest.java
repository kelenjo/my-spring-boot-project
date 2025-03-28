package ge.giorgi.springbootdemo.cars;

import ge.giorgi.springbootdemo.car.cloudinary.CloudinaryService;
import ge.giorgi.springbootdemo.car.dto.PaginationRequest;
import ge.giorgi.springbootdemo.car.dto.PriceRequest;
import ge.giorgi.springbootdemo.car.error.NotFoundException;
import ge.giorgi.springbootdemo.car.models.CarDTO;
import ge.giorgi.springbootdemo.car.models.CarRequest;
import ge.giorgi.springbootdemo.car.persistence.Car;
import ge.giorgi.springbootdemo.car.persistence.CarRepository;
import ge.giorgi.springbootdemo.car.CarService;
import ge.giorgi.springbootdemo.car.EngineService;
import ge.giorgi.springbootdemo.car.persistence.Engine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private EngineService engineService;

    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private CarService carService;

    @Test
    void deleteCar_shouldDeleteCar_whenCarExists() {
        when(carRepository.existsById(1L)).thenReturn(true);

        carService.deleteCar(1L);

        verify(carRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteCar_shouldThrowNotFoundException_whenCarDoesNotExist() {
        when(carRepository.existsById(1L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> carService.deleteCar(1L));

        verify(carRepository, never()).deleteById(anyLong());
    }

    @Test
    void deleteCar_shouldCheckExistenceBeforeDeleting() {
        when(carRepository.existsById(1L)).thenReturn(true);

        carService.deleteCar(1L);

        InOrder inOrder = inOrder(carRepository);
        inOrder.verify(carRepository).existsById(1L);
        inOrder.verify(carRepository).deleteById(1L);
    }

    @Test
    void getCars_shouldReturnPagedCars() {
        PaginationRequest paginationRequest = new PaginationRequest(0, 10);
        Page<CarDTO> expectedPage = new PageImpl<>(List.of());
        when(carRepository.findCars(PageRequest.of(0, 10))).thenReturn(expectedPage);

        Page<CarDTO> result = carService.getCars(paginationRequest);

        assertEquals(expectedPage, result);
    }

    @Test
    void addCar_shouldSaveNewCar() {
        CarRequest carRequest = new CarRequest(12000, "Tesla", 2022, true, 1L,
                "https://res.cloudinary.com/dtxb2v9uk/image/upload/v1741540207/audi_sbxyq0.jpg");
        Car car = new Car();
        Engine engine = new Engine();
        engine.setId(1L);

        when(engineService.findEngine(1L)).thenReturn(engine);
        when(cloudinaryService.imageValidation(any())).thenReturn(
                "https://res.cloudinary.com/dtxb2v9uk/image/upload/v1741540207/audi_sbxyq0.jpg"
        );

        carService.addCar(carRequest);

        verify(carRepository, times(1)).save(any(Car.class));
    }

    @Test
    void updateCar_shouldUpdateExistingCar() {
        Car existingCar = new Car();
        Engine engine = new Engine();
        engine.setId(2L);

        CarRequest carRequest = new CarRequest(335000, "UpdatedModel", 2023, false, 2L,
                "https://res.cloudinary.com/dtxb2v9uk/image/upload/v1741540207/audi_sbxyq0.jpg");
        when(carRepository.findById(1000L)).thenReturn(Optional.of(existingCar));
        when(engineService.findEngine(2L)).thenReturn(engine);
        when(cloudinaryService.imageValidation(anyString())).thenReturn(
                "https://res.cloudinary.com/dtxb2v9uk/image/upload/v1741540207/audi_sbxyq0.jpg"
        );

        carService.updateCar(1000L, carRequest);

        assertEquals("UpdatedModel", existingCar.getModel());
        assertEquals(2023, existingCar.getYear());
        assertEquals(335000, existingCar.getPriceInCents());
        assertEquals(engine, existingCar.getEngine());
        verify(carRepository).save(existingCar);
    }

    @Test
    void updateCar_shouldThrowNotFoundException_whenCarNotFound() {
        CarRequest carRequest = new CarRequest(335000, "UpdatedModel", 2023, false, 2L,
                "https://res.cloudinary.com/dtxb2v9uk/image/upload/v1741540207/audi_sbxyq0.jpg");
        when(carRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> carService.updateCar(1L, carRequest));

        verify(carRepository, never()).save(any(Car.class));
    }

    @Test
    void changeCarPrice_shouldUpdateCarPrice() {
        Car car = new Car();
        PriceRequest priceRequest = new PriceRequest(70000);
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));

        carService.changeCarPrice(1L, priceRequest);

        assertEquals(70000, car.getPriceInCents());
        verify(carRepository, times(1)).save(car);
    }
}
