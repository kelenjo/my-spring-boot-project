package ge.giorgi.springbootdemo.car.persistence;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="car")
@SequenceGenerator(name = "car_seq_gen", sequenceName = "car_seq", allocationSize = 1)
@Getter
@Setter
public class Car {
    @Id
    @GeneratedValue(generator = "car_seq_gen", strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "model")
    private String model;

    @Column(name = "year")
    private int year;

    @Column(name = "is_driving")
    private boolean driveable;

    @ManyToOne
    @JoinColumn(name = "engine_id")
    private Engine engine;

    @Column(name = "priceincents")
    private long priceInCents;

    @Column(name = "imageid")
    private String imageUrl;



}