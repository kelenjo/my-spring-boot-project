package ge.giorgi.springbootdemo.car.user.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class UserRequest {

    @NotBlank
    @Size(min=5, max=20)
    private String username;

    @NotBlank
    @Size(min=8)
    private String password;

    @PositiveOrZero
    @Max(Long.MAX_VALUE)// magdeni vis eqneba dd
    private long balanceInCents;

    @NotEmpty
    private Set<Long> roleIds;


}
