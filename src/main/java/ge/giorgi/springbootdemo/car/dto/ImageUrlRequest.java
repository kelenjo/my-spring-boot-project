package ge.giorgi.springbootdemo.car.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageUrlRequest {

    @NotNull(message = "Image URL cannot be null")
    private String imageUrl;

}
