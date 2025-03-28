package ge.giorgi.springbootdemo.gaming.persistence;

import ge.giorgi.springbootdemo.gaming.models.PersonDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query("SELECT NEW ge.giorgi.springbootdemo.gaming.models.PersonDTO(p.id, p.name, p.age)" +
            "FROM Person p")
    Page<PersonDTO> findAllPersons(Pageable pageable);

    @Query("SELECT COUNT(c) > 0 FROM Company c WHERE c.owner.id = :personId")
    boolean hasCompany(long personId);

}
