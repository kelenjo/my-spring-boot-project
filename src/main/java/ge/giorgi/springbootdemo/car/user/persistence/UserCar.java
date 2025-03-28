package ge.giorgi.springbootdemo.car.user.persistence;

import ge.giorgi.springbootdemo.car.persistence.Car;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "user_car")
@SequenceGenerator(name = "user_car_seq_gen", sequenceName = "user_car_seq", allocationSize = 1)
@Getter
@Setter
public class UserCar {

    @Id
    @GeneratedValue(generator = "user_car_seq_gen", strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @Column(name = "boughtfor")
    private Long boughtFor;

    @Column(name = "purchase_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP) // ✅ Ensures correct handling of TIMESTAMP type
    private Instant purchaseTime;


    @Column(name = "sellingfor")
    private Long sellingFor;
}
