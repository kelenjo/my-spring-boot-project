package ge.giorgi.springbootdemo.car.user;

import ge.giorgi.springbootdemo.car.CarService;
import ge.giorgi.springbootdemo.car.dto.PaginationRequest;
import ge.giorgi.springbootdemo.car.error.NotFoundException;
import ge.giorgi.springbootdemo.car.models.CarDTO;
import ge.giorgi.springbootdemo.car.persistence.Car;
import ge.giorgi.springbootdemo.car.user.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserCarService {

    private final UserCarRepository userCarRepository;
    private final CarService carService;

    public void addCar(AppUser user, Car car){
        UserCar userCar = new UserCar();
        userCar.setUser(user);
        userCar.setCar(car);
        userCar.setPurchaseTime(Instant.now());
        userCar.setBoughtFor(car.getPriceInCents());
        userCar.setSellingFor(null);
        userCarRepository.save(userCar);
    }

    public Page<OwnedCarsDTO> getUserCars(PaginationRequest request, Long userId){
        return userCarRepository.findByUser_Id(PageRequest.of(request.getPage(), request.getPageSize()), userId);
    }

    public Page<OwnedCarsDTO> getAllUserCars(PaginationRequest request){
        return userCarRepository.findAllCars(PageRequest.of(request.getPage(), request.getPageSize()));
    }

    public UserCar findById(long ownedCarId){
        return userCarRepository.findById(ownedCarId).orElseThrow(
                () -> new NotFoundException("OwnedCarId " + ownedCarId + " was not found"));
    }

    public void save(UserCar userCar){
        userCarRepository.save(userCar);
    }

    public Page<CarDTO> getAllCompanySellingCars(PaginationRequest paginationRequest){
        return carService.getCars(paginationRequest);
    }

    public Page<OwnedCarsDTO> getAllUserSellingCars(PaginationRequest request){
        return userCarRepository.findAllSellingCars(PageRequest.of(request.getPage(), request.getPageSize()));
    }

}
