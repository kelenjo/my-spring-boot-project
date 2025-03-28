package ge.giorgi.springbootdemo.cars;

import org.mockito.*;
import ge.giorgi.springbootdemo.car.EngineService;
import ge.giorgi.springbootdemo.car.dto.PaginationRequest;
import ge.giorgi.springbootdemo.car.error.EngineInUseException;
import ge.giorgi.springbootdemo.car.error.NotFoundException;
import ge.giorgi.springbootdemo.car.models.EngineDTO;
import ge.giorgi.springbootdemo.car.models.EngineRequest;
import ge.giorgi.springbootdemo.car.persistence.Engine;
import ge.giorgi.springbootdemo.car.persistence.EngineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.PageImpl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class EngineServiceTest {

    @Mock
    private EngineRepository engineRepository;

    @InjectMocks
    private EngineService engineService;

    private Engine engine;
    private EngineDTO engineDTO;
    private EngineRequest engineRequest;


    @BeforeEach
    void setUp() {
        engine = new Engine();
        engine.setId(1L);
        engine.setHorsePower(200);
        engine.setCapacity(2.5);

        engineRequest = new EngineRequest(250, 0.3);
    }

    @Test
    void getEngineDTOs_shouldReturnPagedEngineDTOs() {
        PaginationRequest paginationRequest = new PaginationRequest(0, 10);
        Page<EngineDTO> page = new PageImpl<>(Collections.singletonList(engineDTO));

        when(engineRepository.findEngines(PageRequest.of(
                paginationRequest.getPage(), paginationRequest.getPageSize()), 2.5)).thenReturn(page);

        Page<EngineDTO> result = engineService.getEngines(paginationRequest, 2.5);
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(engineRepository, times(1)).findEngines(any(), eq(2.5));
    }

    @Test
    void getEngine_shouldReturnEngineDTO_whenEngineExists() {
        when(engineRepository.findById(1L)).thenReturn(Optional.of(engine));

        EngineDTO result = engineService.getEngine(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(200, result.getHorsePower());
        verify(engineRepository, times(1)).findById(1L);
    }

    @Test
    void getEngine_shouldThrowNotFoundException_whenEngineDoesNotExist() {
        when(engineRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> engineService.getEngine(1L));
        assertEquals("engine with id: 1 was not found", thrown.getMessage());
    }

    @Test
    void createEngine_shouldCreateNewEngine() {
        engineService.createEngine(engineRequest);

        verify(engineRepository, times(1)).save(any(Engine.class));
    }

    @Test
    void deleteEngine_shouldDeleteEngine_whenEngineExistsAndNotInUse() {
        when(engineRepository.existsById(1L)).thenReturn(true);
        when(engineRepository.existsCarByEngineId(1L)).thenReturn(false);

        engineService.deleteEngine(1L);

        verify(engineRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteEngine_shouldThrowEngineInUseException_whenEngineIsInUse() {
        when(engineRepository.existsById(1L)).thenReturn(true);
        when(engineRepository.existsCarByEngineId(1L)).thenReturn(true);

        EngineInUseException thrown = assertThrows(EngineInUseException.class, () -> engineService.deleteEngine(1L));
        assertEquals("Engine with id: 1 is referenced by a car and cannot be deleted.", thrown.getMessage());
    }

    @Test
    void deleteEngine_shouldThrowNotFoundException_whenEngineDoesNotExist() {
        when(engineRepository.existsById(1L)).thenReturn(false);

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> engineService.deleteEngine(1L));
        assertEquals("engine with id: 1 was not found", thrown.getMessage());
    }

    @Test
    void updateEngine_shouldUpdateEngine() {
        when(engineRepository.findById(1L)).thenReturn(Optional.of(engine));
        when(engineRepository.save(any(Engine.class))).thenReturn(engine);

        EngineDTO result = engineService.updateEngine(1L, engineRequest);

        assertNotNull(result);
        assertEquals(250, result.getHorsePower());
        assertEquals(0.3, result.getCapacity());
        verify(engineRepository, times(1)).save(any(Engine.class));
    }

    @Test
    void updateEngine_shouldThrowNotFoundException_whenEngineDoesNotExist() {
        when(engineRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> engineService.updateEngine(1L, engineRequest));
        assertEquals("engine with id: 1 was not found", thrown.getMessage());
    }
}
