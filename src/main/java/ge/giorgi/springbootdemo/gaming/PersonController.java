package ge.giorgi.springbootdemo.gaming;

import ge.giorgi.springbootdemo.gaming.models.PersonDTO;
import ge.giorgi.springbootdemo.gaming.models.PersonRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService){
        this.personService=personService;
    }

    @PostMapping
    public ResponseEntity<PersonDTO> addPerson(@RequestBody PersonRequest personRequest){
        PersonDTO personDTO=personService.addPerson(personRequest);
        if(personDTO!=null){
            return ResponseEntity.status(HttpStatus.CREATED).body(personDTO);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<PersonDTO> updatePerson(@PathVariable long id, @RequestBody PersonRequest personRequest){
        PersonDTO personDTO=personService.updatePerson(id, personRequest);
        if(personDTO!=null){
            return ResponseEntity.ok(personDTO);
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("{personId}")
    public ResponseEntity<String> deletePerson(@PathVariable long personId) {
        Boolean result = personService.deletePerson(personId);

        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person not found.");
        } else if (!result) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot delete: Person is an owner of a company.");
        } else {
            return ResponseEntity.ok("Person deleted successfully.");
        }
    }

    @GetMapping
    public Page<PersonDTO> getPersons(@RequestParam int page, @RequestParam int pageSize){
        return personService.getPersons(page, pageSize);
    }

    @GetMapping("{id}")
    public ResponseEntity<PersonDTO> getPerson(@PathVariable long id){
        PersonDTO personDTO=personService.findPersonDTO(id);
        if(personDTO!=null){
            return ResponseEntity.ok(personDTO);
        }
        return ResponseEntity.notFound().build();
    }
}
