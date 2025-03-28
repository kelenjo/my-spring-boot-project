package ge.giorgi.springbootdemo.car.user;


import ge.giorgi.springbootdemo.car.CarService;
import ge.giorgi.springbootdemo.car.dto.PaginationRequest;
import ge.giorgi.springbootdemo.car.dto.PriceRequest;
import ge.giorgi.springbootdemo.car.error.AccessDeniedException;
import ge.giorgi.springbootdemo.car.error.InvalidPurchasingException;
import ge.giorgi.springbootdemo.car.error.NotEnoughMoneyException;
import ge.giorgi.springbootdemo.car.error.NotFoundException;
import ge.giorgi.springbootdemo.car.models.CarDTO;
import ge.giorgi.springbootdemo.car.persistence.Car;
import ge.giorgi.springbootdemo.car.user.persistence.AppUser;
import ge.giorgi.springbootdemo.car.user.persistence.UserCar;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

import static ge.giorgi.springbootdemo.car.security.AuthUtil.*;

@Service
@RequiredArgsConstructor
public class UserCarFacadeService {
    private final UserService userService;
    private final UserCarService userCarService;
    private final CarService carService;


    public Page<OwnedCarsDTO> getUserCars(PaginationRequest paginationRequest, Optional<Long> userId) {
        AppUser user = userService.findByUsername(getAuthenticatedUser());
        if(userId.isPresent()) {
            if (!hasAdminRole() && !userId.get().equals(user.getId())) {
                throw new AccessDeniedException("User can not access others cars");
            }else if(userService.findById(userId.get())!=null){
                return userCarService.getUserCars(paginationRequest, userId.get());
            }
        }
        return userCarService.getUserCars(paginationRequest, user.getId());
    }

    public Page<OwnedCarsDTO> getAllUserCars(PaginationRequest paginationRequest){
        return userCarService.getAllUserCars(paginationRequest);
    }

    public void buyCarFromCompany(long carId){
        AppUser user = userService.findByUsername(getAuthenticatedUser());
        Car car = carService.findCar(carId);

        long userBalance=user.getBalanceInCents(), carPrice=car.getPriceInCents();
        if(userBalance>=carPrice){
            user.setBalanceInCents(userBalance-carPrice);
            userCarService.addCar(user, car);
            userService.save(user);
        }else{
            throw new NotEnoughMoneyException("User " + user.getId() + " can not afford car: " + car.getId());
        }
    }

    public void buyCarFromUser(long ownedCarId){
        AppUser user = userService.findByUsername(getAuthenticatedUser());
        UserCar userCar = userCarService.findById(ownedCarId);
        AppUser sellerUser = userCar.getUser();

        if(user.getId().equals(sellerUser.getId())){
            throw new InvalidPurchasingException("User " + user.getUsername() + " is in his property already");
        }
        if(userCar.getSellingFor()==null){
            throw new NotFoundException("UserCar " + ownedCarId + " was not found for sale");
        }
        long userBalance=user.getBalanceInCents(), carPrice=userCar.getSellingFor();
        if(userBalance>=carPrice){
            user.setBalanceInCents(userBalance-carPrice);
            sellerUser.setBalanceInCents(sellerUser.getBalanceInCents()+carPrice);
            userCar.setUser(user);
            userCar.setBoughtFor(carPrice);
            userCar.setPurchaseTime(Instant.now());
            userCar.setSellingFor(null);
            userCarService.save(userCar);
            userService.save(user);
            userService.save(sellerUser);

        }else{
            throw new NotEnoughMoneyException("User " + user.getId() + " can not afford car: " + userCar.getCar().getId());
        }
    }

    public void sellCar(long ownedCarId, PriceRequest priceRequest){
        AppUser user = userService.findByUsername(getAuthenticatedUser());
        UserCar userCar = userCarService.findById(ownedCarId);
        if(userCar.getUser().getId().equals(user.getId())) {
            userCar.setSellingFor(priceRequest.getPriceInCents());
            userCarService.save(userCar);
        }else{
            throw new AccessDeniedException("User " + user.getUsername() + " can not sell others cars!");
        }
    }

    public void removeCarFromSelling(long ownedCarId){
        AppUser user = userService.findByUsername(getAuthenticatedUser());
        UserCar userCar = userCarService.findById(ownedCarId);
        if(userCar.getUser().getId().equals(user.getId())) {
            if(userCar.getSellingFor()!=null) {
                userCar.setSellingFor(null);
                userCarService.save(userCar);
            }else{
                throw new NotFoundException(userCar.getId() + " Is not for selling");
            }
        }else{
            throw new AccessDeniedException("User " + user.getUsername() + " can not remove others cars from market!");
        }
    }



}
