package ge.giorgi.springbootdemo.car.user.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "role", schema = "cars")
@Getter
@Setter
@RequiredArgsConstructor
public class Role {

    @Id
    private Long id;

    @Column
    private String name;
}