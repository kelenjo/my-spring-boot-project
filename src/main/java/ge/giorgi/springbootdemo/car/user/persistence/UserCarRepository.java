package ge.giorgi.springbootdemo.car.user.persistence;

import ge.giorgi.springbootdemo.car.user.OwnedCarsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserCarRepository extends JpaRepository<UserCar, Long> {

    @Query("SELECT NEW ge.giorgi.springbootdemo.car.user.OwnedCarsDTO(uc.id, uc.user.id, " +
            "NEW ge.giorgi.springbootdemo.car.models.CarDTO(uc.car.id, uc.car.imageUrl, uc.car.priceInCents, uc.car.model, uc.car.year, uc.car.driveable, " +
            "NEW ge.giorgi.springbootdemo.car.models.EngineDTO(uc.car.engine.id, uc.car.engine.horsePower, uc.car.engine.capacity)), " +
            "uc.boughtFor, uc.purchaseTime, uc.sellingFor) " +
            "FROM UserCar uc WHERE uc.user.id = :userId")
    Page<OwnedCarsDTO> findByUser_Id(Pageable pageable, Long userId);

    @Query("SELECT NEW ge.giorgi.springbootdemo.car.user.OwnedCarsDTO(uc.id, uc.user.id, " +
            "NEW ge.giorgi.springbootdemo.car.models.CarDTO(uc.car.id, uc.car.imageUrl, uc.car.priceInCents, uc.car.model, uc.car.year, uc.car.driveable, " +
            "NEW ge.giorgi.springbootdemo.car.models.EngineDTO(uc.car.engine.id, uc.car.engine.horsePower, uc.car.engine.capacity)), " +
            "uc.boughtFor, uc.purchaseTime, uc.sellingFor) " +
            "FROM UserCar uc")
    Page<OwnedCarsDTO> findAllCars(Pageable pageable);

    @Query("SELECT NEW ge.giorgi.springbootdemo.car.user.OwnedCarsDTO(uc.id, uc.user.id, " +
            "NEW ge.giorgi.springbootdemo.car.models.CarDTO(uc.car.id, uc.car.imageUrl, uc.car.priceInCents, uc.car.model, uc.car.year, uc.car.driveable, " +
            "NEW ge.giorgi.springbootdemo.car.models.EngineDTO(uc.car.engine.id, uc.car.engine.horsePower, uc.car.engine.capacity)), " +
            "uc.boughtFor, uc.purchaseTime, uc.sellingFor) " +
            "FROM UserCar uc WHERE uc.sellingFor >= 0")
    Page<OwnedCarsDTO> findAllSellingCars(Pageable pageable);


}
