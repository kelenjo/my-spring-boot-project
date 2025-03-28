package ge.giorgi.springbootdemo.car.models;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
//@AllArgsConstructor
public class CarDTO {
    private long id;
    private String imageUrl;
    private double priceInDollars;
    private String model;
    private int year;
    private boolean driveable;
    private EngineDTO engineDTO;

    public CarDTO(long id, String imageUrl, double priceInDollars, String model, int year, boolean driveable, EngineDTO engineDTO) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.priceInDollars = (double) priceInDollars/100;
        this.model = model;
        this.year = year;
        this.driveable = driveable;
        this.engineDTO = engineDTO;
    }






}
