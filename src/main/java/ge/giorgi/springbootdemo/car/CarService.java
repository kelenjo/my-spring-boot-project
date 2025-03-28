package ge.giorgi.springbootdemo.car;


import ge.giorgi.springbootdemo.car.cloudinary.CloudinaryService;
import ge.giorgi.springbootdemo.car.dto.ImageUrlRequest;
import ge.giorgi.springbootdemo.car.dto.PaginationRequest;
import ge.giorgi.springbootdemo.car.dto.PriceRequest;
import ge.giorgi.springbootdemo.car.error.ImageAlreadyExistsException;
import ge.giorgi.springbootdemo.car.error.NotFoundException;
import ge.giorgi.springbootdemo.car.models.CarDTO;
import ge.giorgi.springbootdemo.car.models.CarRequest;
import ge.giorgi.springbootdemo.car.models.EngineDTO;
import ge.giorgi.springbootdemo.car.persistence.Car;
import ge.giorgi.springbootdemo.car.persistence.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final EngineService engineService;
    private final CloudinaryService cloudinaryService;

//    public List<CarDTO> getCars() {
//        return carRepository.findAll().stream().map(this::mapCar).collect(Collectors.toList());
//    }

    public Page<CarDTO> getCars(PaginationRequest paginationRequest) {
        return carRepository.findCars(PageRequest.of(paginationRequest.getPage(), paginationRequest.getPageSize()));
    }

    public void setImage(Long carId, ImageUrlRequest imageUrl){
        Car car=findCar(carId);
        String image = cloudinaryService.imageValidation(imageUrl.getImageUrl());
        if(image.equals(car.getImageUrl())){
            throw new ImageAlreadyExistsException("Car already has image: "+ image);
        }
        if(!carRepository.findByImage(image)){
            car.setImageUrl(image);
            carRepository.save(car);
        }else{
            throw new ImageAlreadyExistsException("Each car must have unique image");
        }
    }

    public void addCar(CarRequest carRequest) {
        Car car = new Car();
        car.setModel(carRequest.getModel());
        car.setYear(carRequest.getYear());
        car.setDriveable(carRequest.isDriveable());
        car.setEngine(engineService.findEngine(carRequest.getEngineId()));
        car.setPriceInCents(carRequest.getPriceInCents());
        car.setImageUrl(cloudinaryService.imageValidation(carRequest.getImageUrl()));
        carRepository.save(car);
    }

    public void updateCar(long id, CarRequest carRequest) {
        Car car=carRepository.findById(id).orElseThrow(() -> buildNotFoundException(id));
        car.setModel(carRequest.getModel());
        car.setYear(carRequest.getYear());
        car.setDriveable(carRequest.isDriveable());
        car.setEngine(engineService.findEngine(carRequest.getEngineId()));
        car.setPriceInCents(carRequest.getPriceInCents());
        car.setImageUrl(cloudinaryService.imageValidation(carRequest.getImageUrl()));
        carRepository.save(car);

    }

    public void changeCarPrice(long id, PriceRequest priceRequest){
        Car car=carRepository.findById(id).orElseThrow(() -> buildNotFoundException(id));
        car.setPriceInCents(priceRequest.getPriceInCents());
        carRepository.save(car);
    }

    public void deleteCar(long id) {
        if(!carRepository.existsById(id))
            throw buildNotFoundException(id);
        carRepository.deleteById(id);
    }

    public CarDTO getCar(long id){
        Car car=findCar(id);
        return mapCar(car);
    }
    public Car findCar(long id) {
        return carRepository.findById(id).orElseThrow(() -> buildNotFoundException(id));
    }

    private CarDTO mapCar(Car car){
        return new  CarDTO(car.getId(), car.getImageUrl(), car.getPriceInCents() ,car.getModel(), car.getYear(), car.isDriveable(),
                new EngineDTO(car.getEngine().getId(), car.getEngine().getHorsePower(), car.getEngine().getCapacity()));
    }

    private NotFoundException buildNotFoundException(Long id){
        return new NotFoundException("car with id: "+ id + " was not found");
    }
}
