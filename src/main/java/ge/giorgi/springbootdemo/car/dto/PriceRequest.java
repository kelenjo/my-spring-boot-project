package ge.giorgi.springbootdemo.car.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PriceRequest {
    @NotNull
    @Min(0)
    @Max(Long.MAX_VALUE)
    private long priceInCents;
}
