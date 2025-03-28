package ge.giorgi.springbootdemo.car;


import ge.giorgi.springbootdemo.car.cloudinary.CloudinaryService;
import ge.giorgi.springbootdemo.car.dto.ImageUrlRequest;
import ge.giorgi.springbootdemo.car.dto.PaginationRequest;
import ge.giorgi.springbootdemo.car.dto.PriceRequest;
import ge.giorgi.springbootdemo.car.models.CarDTO;
import ge.giorgi.springbootdemo.car.models.CarRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static ge.giorgi.springbootdemo.car.security.AuthUtil.ADMIN;
import static ge.giorgi.springbootdemo.car.security.AuthUtil.USER_OR_ADMIN;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;
    private final CloudinaryService cloudinaryService;

    @PostMapping
    @PreAuthorize(ADMIN)
    public ResponseEntity<String> addCar(@RequestBody @Valid CarRequest carRequest) {
        carService.addCar(carRequest);
        return ResponseEntity.ok("Car added successfully!");
    }

    @PutMapping("{id}")
    @PreAuthorize(ADMIN)
    public ResponseEntity<String> updateCar(@PathVariable long id, @RequestBody @Valid CarRequest carRequest) {
        carService.updateCar(id, carRequest);
        return ResponseEntity.ok("Car updated successfully!");
    }

    @PutMapping("{id}/changePrice")
    @PreAuthorize(ADMIN)
    public ResponseEntity<String> changeCarPrice(@PathVariable long id, @RequestBody @Valid PriceRequest priceRequest){
        carService.changeCarPrice(id, priceRequest);
        return ResponseEntity.ok("Car price has changed to: "+ (double)priceRequest.getPriceInCents()/100);
    }

    @PutMapping("/{carId}/setImage")
    @PreAuthorize(ADMIN)
    public ResponseEntity<String> setImageForCar(@PathVariable Long carId, @RequestBody @Valid ImageUrlRequest imageUrl) {
        carService.setImage(carId, imageUrl);
        return ResponseEntity.ok("Image was successfully setted to car: "+ carId);
    }

    @PutMapping("/uploadImageInCloudinary")
    @PreAuthorize(ADMIN)
    public ResponseEntity<String> uploadImageForCar(@RequestParam("file") MultipartFile file) {
        String imageUrl = cloudinaryService.uploadImage(file);
        return ResponseEntity.ok("Image uploaded successfully! Image URL: " + imageUrl);
    }

    @DeleteMapping("{id}")
    @PreAuthorize(ADMIN)
    public ResponseEntity<String> deleteCar(@PathVariable long id) {
        carService.deleteCar(id);
        return ResponseEntity.ok("Car deleted successfully!");
    }

    @GetMapping("{id}")
    @PreAuthorize(USER_OR_ADMIN)
    ResponseEntity<CarDTO> getCar(@PathVariable long id){
        return ResponseEntity.ok(carService.getCar(id));
    }
//    @GetMapping
//    public List<CarDTO> getCars(){
//        return carService.getCars();
//    }

    @GetMapping
    @PreAuthorize(USER_OR_ADMIN)
    public Page<CarDTO> getCars(@RequestBody @Valid PaginationRequest paginationRequest){
        return carService.getCars(paginationRequest);
    }
}
