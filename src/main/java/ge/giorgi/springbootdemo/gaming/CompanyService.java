package ge.giorgi.springbootdemo.gaming;

import ge.giorgi.springbootdemo.gaming.models.*;
import ge.giorgi.springbootdemo.gaming.persistence.Company;
import ge.giorgi.springbootdemo.gaming.persistence.CompanyRepository;
import ge.giorgi.springbootdemo.gaming.persistence.Game;
import ge.giorgi.springbootdemo.gaming.persistence.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final PersonService personService;
    private final GameService gameService;

    public CompanyService(CompanyRepository companyRepository, PersonService personService, GameService gameService) {
        this.companyRepository = companyRepository;
        this.personService=personService;
        this.gameService=gameService;
    }

    public CompanyDTO findCompany(long id){
        Company company = companyRepository.findById(id).orElse(null);
        if(company!=null){
            return mapCompany(company);
        }
        return null;
    }

    public CompanyDTO mapCompany(Company company){
        return new CompanyDTO(
                company.getId(),
                company.getName(),
                personService.mapPerson(company.getOwner()),
                gameService.mapToGameWithoutCompanyDTO(company.getGames()));
    }

    public GameWithoutCompanyDTO addGame(long id, GameRequest gameRequest){
        Company company = companyRepository.findById(id).orElse(null);
        if(company!=null) {
            Game game=new Game();
            game.setTitle(gameRequest.getTitle());
            game.setGenre(gameRequest.getGenre());
            game.setReleaseYear(gameRequest.getReleaseYear());
            game.setCompany(company);
            return gameService.addGame(game);
        }
        return null;
    }

    public CompanyDTO addCompany(CompanyRequest companyRequest){
        Company company=new Company();
        company.setName(companyRequest.getName());
        Person person=personService.findPerson(companyRequest.getOwnerId());
        if(person!=null){
            company.setOwner(person);
            companyRepository.save(company);
            return mapCompany(company);
        }
        return null;
    }

    public CompanyDTO updateName(long id, String name){
        Company company = companyRepository.findById(id).orElse(null);
        if(company!=null){
            company.setName(name);
            companyRepository.save(company);
            return mapCompany(company);
        }
        return null;
    }

    public CompanyDTO updateOwner(long id, long personId){
        Company company=companyRepository.findById(id).orElse(null);
        if(company!=null && company.getOwner().getId()!=personId){
            Person person = personService.findPerson(personId);
            if (person != null) {
                company.setOwner(person);
                companyRepository.save(company);
                return mapCompany(company);
            }
        }
        return null;
    }

    public boolean deleteCompany(long id){
        Company company=companyRepository.findById(id).orElse(null);
        if(company!=null){
//            gameService.deleteCompanyGames(id); cven cascadetype.all viyenebt
            companyRepository.delete(company);
            return true;
        }
        return false;
    }

    public boolean deleteGameInCompany(long id, long gameId){
        Company company=companyRepository.findById(id).orElse(null);
        if(company!=null){
            return gameService.deleteGame(gameId);
        }
        return false;
    }

    public GameWithoutCompanyDTO getGame(long id, long gameId){
        CompanyDTO companyDTO=findCompany(id);
        if(companyDTO!=null){
            GameDTO gameDTO=gameService.findGame(gameId);
            if(gameDTO!=null && gameDTO.getCompanyId()==companyDTO.getId()){
                return new GameWithoutCompanyDTO(
                        gameDTO.getId(),
                        gameDTO.getTitle(),
                        gameDTO.getGenre(),
                        gameDTO.getReleaseYear()
                );
            }
        }
        return null;
    }

    public List<GameWithoutCompanyDTO> getGames(long id){
        CompanyDTO companyDTO=findCompany(id);
        if(companyDTO!=null){
            return gameService.getGamesByCompanyId(id);
        }
        return null;
    }

    public PersonDTO getOwner(long companyId){
        CompanyDTO companyDTO=findCompany(companyId);
        if(companyDTO!=null){
            return companyDTO.getOwner();
        }return null;
    }

//    public List<CompanyDTO> getAllCompanies() {
//        return companyRepository.findAll().stream().map(this::mapCompany).collect(Collectors.toList());
//    }

    public Page<CompanyDTO> getAllCompanies(int page, int pageSize) {
        return companyRepository.findAllCompanies(PageRequest.of(page, pageSize)).map(this::mapCompany);
    }
}
