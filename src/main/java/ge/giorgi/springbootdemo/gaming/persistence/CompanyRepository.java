package ge.giorgi.springbootdemo.gaming.persistence;

import ge.giorgi.springbootdemo.gaming.models.CompanyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    // Fetch all companies along with their owners and games // research
//    @Override
//    @EntityGraph(attributePaths = {"owner", "games"})
//    List<Company> findAll();

    @EntityGraph(attributePaths = {"owner", "games"})
    @Query("SELECT c FROM Company c")
    Page<Company> findAllCompanies(Pageable pageable);




}
