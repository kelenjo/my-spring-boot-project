package ge.giorgi.springbootdemo.gaming;


import ge.giorgi.springbootdemo.gaming.models.*;
import jakarta.websocket.server.PathParam;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService){
        this.companyService=companyService;
    }

    @PostMapping("{id}/addGame")
    public ResponseEntity<GameWithoutCompanyDTO> addGame(@PathVariable long id, @RequestBody GameRequest gameRequest){
        GameWithoutCompanyDTO gameWithoutCompanyDTO=companyService.addGame(id, gameRequest);
        if(gameWithoutCompanyDTO!=null){
            return ResponseEntity.status(HttpStatus.CREATED).body(gameWithoutCompanyDTO);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping
    public ResponseEntity<CompanyDTO> addCompany(@RequestBody CompanyRequest companyRequest){
        CompanyDTO companyDTO=companyService.addCompany(companyRequest);
        if(companyDTO!=null){
            return ResponseEntity.status(HttpStatus.CREATED).body(companyDTO);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("{id}/changeName")
    public ResponseEntity<CompanyDTO> updateCompanyName(@PathVariable long id, @RequestParam String name){
        CompanyDTO companyDTO=companyService.updateName(id, name);
        if(companyDTO!=null){
            return ResponseEntity.ok(companyDTO);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("{id}/changeOwner")
    public ResponseEntity<CompanyDTO> updateCompanyOwner(@PathVariable long id, @RequestParam long personId){
        CompanyDTO companyDTO=companyService.updateOwner(id, personId);
        if(companyDTO!=null){
            return ResponseEntity.ok(companyDTO);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{companyId}")
    public ResponseEntity<String> deleteCompany(@PathVariable long companyId) {
        boolean gamesDeleted = companyService.deleteCompany(companyId);

        if (gamesDeleted) {
            return ResponseEntity.ok("Company and its games were deleted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}/delete/{gameId}")
    public ResponseEntity<String> deleteGame(@PathVariable long id, @PathVariable long gameId){
        if(companyService.deleteGameInCompany(id, gameId)){
            return ResponseEntity.ok("Game was successfully deleted.");
        }else{
            return ResponseEntity.notFound().build();
        }
    }
//    @GetMapping
//    public List<CompanyDTO> getCompanies(){
//        return companyService.getAllCompanies();
//    }
    @GetMapping
    public Page<CompanyDTO> getCompanies(@RequestParam int page, @RequestParam int pageSize){
        return companyService.getAllCompanies(page, pageSize);
    }

    @GetMapping("{id}")
    public ResponseEntity<CompanyDTO> getCompany(@PathVariable long id){
        CompanyDTO companyDTO=companyService.findCompany(id);
        if(companyDTO!=null){
            return ResponseEntity.ok(companyDTO);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("{id}/games")
    public List<GameWithoutCompanyDTO> getGamesFromCompany(@PathVariable long id){
        return companyService.getGames(id);
    }

    @GetMapping("{id}/{gameId}")
    public ResponseEntity<GameWithoutCompanyDTO> getGame(@PathVariable long id, @PathVariable long gameId){
        GameWithoutCompanyDTO gameWithoutCompanyDTO=companyService.getGame(id, gameId);
        if(gameWithoutCompanyDTO!=null){
            return ResponseEntity.ok(gameWithoutCompanyDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("{id}/owner")
    public ResponseEntity<PersonDTO> getOwner(@PathVariable long id){
         PersonDTO personDTO=companyService.getOwner(id);
         if(personDTO!=null){
             return ResponseEntity.ok(personDTO);
         }
         return ResponseEntity.notFound().build();
    }


}
