package ge.giorgi.springbootdemo.car.user;

import ge.giorgi.springbootdemo.car.dto.PaginationRequest;
import ge.giorgi.springbootdemo.car.dto.PriceRequest;
import ge.giorgi.springbootdemo.car.models.CarDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static ge.giorgi.springbootdemo.car.security.AuthUtil.USER_OR_ADMIN;

@RestController
@RequestMapping("/market")
@RequiredArgsConstructor
public class UserCarController {

    private final UserCarFacadeService userCarFacadeService;
    private final UserCarService userCarService;

    @GetMapping("companyCars")
    @PreAuthorize(USER_OR_ADMIN)
    public Page<CarDTO> getAllCompanySellingCars(@RequestBody @Valid PaginationRequest paginationRequest){
        return userCarService.getAllCompanySellingCars(paginationRequest);
    }

    @GetMapping("userCars")
    @PreAuthorize(USER_OR_ADMIN)
    public Page<OwnedCarsDTO> getAllUserSellingCars(@RequestBody @Valid PaginationRequest paginationRequest){
        return userCarService.getAllUserSellingCars(paginationRequest);
    }

    @PostMapping("/companyCars/buy/{carId}")
    @PreAuthorize(USER_OR_ADMIN)
    public ResponseEntity<String> buyCarFromCompany(@PathVariable Long carId) {
        userCarFacadeService.buyCarFromCompany(carId);
        return ResponseEntity.ok("User " + " successfully bought car " + carId);
    }

    @PostMapping("/userCars/buy/{ownedCarId}")
    @PreAuthorize(USER_OR_ADMIN)
    public ResponseEntity<String> buyCarFromUser(@PathVariable Long ownedCarId) {
        userCarFacadeService.buyCarFromUser(ownedCarId);
        return ResponseEntity.ok("User " + " successfully bought car " + ownedCarId);
    }

    @PutMapping("/sell/{ownedCarId}")
    @PreAuthorize(USER_OR_ADMIN)
    public ResponseEntity<String> sellCar(@PathVariable long ownedCarId, @RequestBody @Valid PriceRequest priceRequest){
        userCarFacadeService.sellCar(ownedCarId, priceRequest);
        return ResponseEntity.ok("OwnedCar " + ownedCarId +
                " was uploaded to market for " + (double) priceRequest.getPriceInCents()/100);
    }

    @PutMapping("/removeCarFromSelling")
    @PreAuthorize(USER_OR_ADMIN)
    public ResponseEntity<String> removeCarFromSelling(@RequestParam long ownedCarId){
        userCarFacadeService.removeCarFromSelling(ownedCarId);
        return ResponseEntity.ok("OwnedCar " + ownedCarId + " was successfully removed in market");
    }
}
