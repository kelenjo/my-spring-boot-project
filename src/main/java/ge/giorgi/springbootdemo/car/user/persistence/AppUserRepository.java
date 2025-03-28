package ge.giorgi.springbootdemo.car.user.persistence;

import ge.giorgi.springbootdemo.car.user.model.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);

    @Override
    Page<AppUser> findAll(Pageable pageable);
}
