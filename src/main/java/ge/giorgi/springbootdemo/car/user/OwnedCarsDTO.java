package ge.giorgi.springbootdemo.car.user;

import ge.giorgi.springbootdemo.car.models.CarDTO;
import lombok.Data;

import java.time.Instant;

@Data
public class OwnedCarsDTO {
    private Long id;
    private Long ownerId;
    private CarDTO car;
    private double boughtFor;
    private Instant purchaseTime;
    private Double sellingFor;

    public OwnedCarsDTO(Long id, Long ownerId, CarDTO car, double boughtFor, Instant purchaseTime, Long sellingFor) {
        this.id = id;
        this.ownerId = ownerId;
        this.car = car;
        this.boughtFor=boughtFor/100;
        this.purchaseTime = purchaseTime;
        this.sellingFor = sellingFor==null ? null : sellingFor.doubleValue()/100;
    }





}
