package ge.giorgi.springbootdemo.gaming;

import ge.giorgi.springbootdemo.gaming.models.PersonDTO;
import ge.giorgi.springbootdemo.gaming.models.PersonRequest;
import ge.giorgi.springbootdemo.gaming.persistence.Person;
import ge.giorgi.springbootdemo.gaming.persistence.PersonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository){
        this.personRepository=personRepository;
    }

    public PersonDTO mapPerson(Person person){
        return new PersonDTO(
                person.getId(),
                person.getName(),
                person.getAge()
        );
    }

    public PersonDTO addPerson(PersonRequest personRequest){
        Person person = new Person();
        person.setName(personRequest.getName());
        person.setAge(personRequest.getAge());

        personRepository.save(person);
        return mapPerson(person);

    }

    public PersonDTO findPersonDTO(long id){
        Person person=findPerson(id);
        if(person!=null){
            return mapPerson(person);
        }
        return null;
    }

    public Person findPerson(long id){
        return personRepository.findById(id).orElse(null);
    }

    public PersonDTO updatePerson(long id, PersonRequest personRequest){
        Person person=personRepository.findById(id).orElse(null);
        if(person!=null){
            person.setName(personRequest.getName());
            person.setAge(personRequest.getAge());
            personRepository.save(person);
            return mapPerson(person);
        }
        return null;
    }

    public Boolean deletePerson(long personId) {
        Person person = personRepository.findById(personId).orElse(null);
        if (person == null) {
            return null; // Person not found
        }
        boolean isOwner = personRepository.hasCompany(personId);
        if (isOwner) {
            return false; // Person is an owner of a company, unda waishalos
        }
        personRepository.deleteById(personId);
        return true; // Successfully deleted
    }

    public Page<PersonDTO> getPersons(int page, int pageSize){
        return personRepository.findAllPersons(PageRequest.of(page, pageSize));
    }




}
