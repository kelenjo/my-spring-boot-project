package ge.giorgi.springbootdemo.car.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private Set<String> roles;
    private double balanceInDollars;

    public UserDTO(Long id, String username, Set<String> roles, double balanceInDollars) {
        this.id = id;
        this.username = username;
        this.roles = roles;
        this.balanceInDollars = balanceInDollars/100;
    }
}
