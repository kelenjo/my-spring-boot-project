package ge.giorgi.springbootdemo.car;


import ge.giorgi.springbootdemo.car.dto.PaginationRequest;
import ge.giorgi.springbootdemo.car.error.EngineInUseException;
import ge.giorgi.springbootdemo.car.error.InvalidPaginationException;
import ge.giorgi.springbootdemo.car.error.NotFoundException;
import ge.giorgi.springbootdemo.car.models.EngineDTO;
import ge.giorgi.springbootdemo.car.models.EngineRequest;
import ge.giorgi.springbootdemo.car.persistence.Engine;
import ge.giorgi.springbootdemo.car.persistence.EngineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EngineService {

    private final EngineRepository engineRepository;

//    public EngineService(EngineRepository engineRepository){
//        this.engineRepository=engineRepository;
//    }

    private EngineDTO mapEngine(Engine engine){
        return new EngineDTO(
                engine.getId(),
                engine.getHorsePower(),
                engine.getCapacity());
    }
    public Page<EngineDTO> getEngines(PaginationRequest request, double capacity) {
        return engineRepository.findEngines(PageRequest.of(request.getPage(), request.getPageSize()), capacity);
    }

    public EngineDTO getEngine(long id){
        Engine engine=findEngine(id);
        return mapEngine(engine);
    }
    public void createEngine(EngineRequest engineRequest) {
        Engine engine = new Engine();
        engine.setHorsePower(engineRequest.getHorsePower());
        engine.setCapacity(engineRequest.getCapacity());
        engineRepository.save(engine);
    }

    public void deleteEngine(long id){
        if(!engineRepository.existsById(id))
            throw buildNotFoundException(id);

        if (engineRepository.existsCarByEngineId(id)) {
            throw new EngineInUseException("Engine with id: " + id + " is referenced by a car and cannot be deleted.");
        }
        engineRepository.deleteById(id);
    }

    public EngineDTO updateEngine(long  id, EngineRequest engineRequest) {
        Engine engine=engineRepository.findById(id).orElseThrow(() -> buildNotFoundException(id));
        engine.setHorsePower(engineRequest.getHorsePower());
        engine.setCapacity(engineRequest.getCapacity());
        engineRepository.save(engine);
        return mapEngine(engine);


    }
    public Engine findEngine(long id){
        return engineRepository.findById(id).orElseThrow(() -> buildNotFoundException(id));
    }

    private NotFoundException buildNotFoundException(Long id){
        return new NotFoundException("engine with id: "+ id + " was not found");
    }
}

