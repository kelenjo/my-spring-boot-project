package ge.giorgi.springbootdemo.car.models;



import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CarRequest {
    @PositiveOrZero
    @Max(Long.MAX_VALUE) // ar girs mara adminma ro ar imaimunos
    private long priceInCents;
    @NotBlank
    @Size(max=20)
    private String model;
    @Min(1940)
    private int year;
    private boolean driveable;
    @Positive
    private Long EngineId;
    @NotBlank
    private String imageUrl;




}
