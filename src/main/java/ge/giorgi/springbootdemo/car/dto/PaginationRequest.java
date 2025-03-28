package ge.giorgi.springbootdemo.car.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaginationRequest {
    @Min(0)
    private int page;
    @Min(1)
    @Max(30)
    private int pageSize;
}
