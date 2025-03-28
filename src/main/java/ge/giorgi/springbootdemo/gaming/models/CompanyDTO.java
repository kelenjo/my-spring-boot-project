package ge.giorgi.springbootdemo.gaming.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class CompanyDTO {
    private long id;
    private String name;
    private PersonDTO owner;
    private List<GameWithoutCompanyDTO> gameWithoutCompanyDTOS;

    public CompanyDTO(){}

    public CompanyDTO(long id, String name, PersonDTO owner){
        this.id=id;
        this.name = name;
        this.owner = owner;
    }
//    public CompanyDTO(long id, String name, PersonDTO owner, List<GameWithoutCompanyDTO> gameWithoutCompanyDTOS) {
//        this.id=id;
//        this.name = name;
//        this.owner = owner;
//        this.gameWithoutCompanyDTOS = new ArrayList<>(gameWithoutCompanyDTOS);
//    }
//    public long getId(){
//        return id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public PersonDTO getOwner() {
//        return owner;
//    }
//
//    public void setOwner(PersonDTO owner) {
//        this.owner = owner;
//    }
//
//    public List<GameWithoutCompanyDTO> getGames() {
//        return gameWithoutCompanyDTOS;
//    }
//
//    public void setGames(List<GameWithoutCompanyDTO> gameWithoutCompanyDTO) {
//        this.gameWithoutCompanyDTOS = gameWithoutCompanyDTOS;
//    }
//
//    // toString Method
//    @Override
//    public String toString() {
//        return "Company{" + "name='" + name + '\'' + ", owner=" + owner + ", games=" + gameWithoutCompanyDTOS.size() + '}';
//    }

}
