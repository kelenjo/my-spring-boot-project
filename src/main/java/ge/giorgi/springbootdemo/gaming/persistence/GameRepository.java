package ge.giorgi.springbootdemo.gaming.persistence;

import ge.giorgi.springbootdemo.gaming.models.GameDTO;
import ge.giorgi.springbootdemo.gaming.models.GameWithoutCompanyDTO;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface GameRepository extends JpaRepository<Game, Long> {

    @Query("SELECT NEW ge.giorgi.springbootdemo.gaming.models.GameWithoutCompanyDTO(g.id, g.title, g.genre, g.releaseYear) " +
            "FROM Game g WHERE g.company.id = :companyId")
    List<GameWithoutCompanyDTO> findGamesByCompanyId(@Param("companyId") Long companyId);

    @Query("SELECT NEW ge.giorgi.springbootdemo.gaming.models.GameDTO(g.id, g.title, g.genre, g.releaseYear, g.company.id)" +
            "FROM Game g")
    Page<GameDTO> findAllGames(Pageable pageable);
//
//    @Transactional
//    @Modifying // I did research, helps java with safer deletion
//    @Query("DELETE FROM Game g WHERE g.company.id = :companyId")
//    void deleteByCompanyId(@Param("companyId") long companyId);





}
