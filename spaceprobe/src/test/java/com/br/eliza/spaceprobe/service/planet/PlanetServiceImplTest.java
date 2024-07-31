package com.br.eliza.spaceprobe.service.planet;

import com.br.eliza.spaceprobe.dto.PlanetDTO;
import com.br.eliza.spaceprobe.dto.RoverDTO;
import com.br.eliza.spaceprobe.exceptions.*;
import com.br.eliza.spaceprobe.model.*;
import com.br.eliza.spaceprobe.repository.PlanetRepository;
import com.br.eliza.spaceprobe.repository.RoverRepository;
import com.br.eliza.spaceprobe.service.coordinate.CoordinateService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;

import static com.br.eliza.spaceprobe.common.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PlanetServiceImplTest{

    @Mock
    private PlanetRepository planetRepo;

    @Mock
    private RoverRepository roverRepo;

    @Mock
    private CoordinateService coordinateService;

    @InjectMocks
    private PlanetServiceImpl planetService;

    @BeforeEach
    public void setUp() {
        planetService = new PlanetServiceImpl(planetRepo, roverRepo, coordinateService);
    }

    @Test
    public void testSave() {
        Planet planet = new Planet();
        planet.setPlanetId(PLANET_ID);
        planet.setPlanetName(PLANET_NAME);
        PlanetDTO planetDTO = new PlanetDTO();
        planetDTO.setPlanetId(PLANET_ID);
        planetDTO.setPlanetName(PLANET_NAME);

        when(planetRepo.save(any(Planet.class))).thenReturn(planet);

        PlanetDTO savedPlanetDTO = planetService.save(planetDTO);
        assertEquals(PLANET_NAME, savedPlanetDTO.getPlanetName());
        verify(planetRepo, times(1)).save(any(Planet.class));
    }

    @Test
    public void testSaveThrowsExceptionWhenPlanetNameIsBlank() {
        PlanetDTO planetDTO = new PlanetDTO();

        Exception exception = assertThrows(RuntimeException.class, () -> {
            planetService.save(planetDTO);
        });

        assertEquals("Planet name must not be informed.", exception.getMessage());
    }

    @Test
    public void testFindAll() {
        Planet planet = new Planet();
        planet.setPlanetId(PLANET_ID);
        planet.setPlanetName(PLANET_NAME);

        when(planetRepo.findAll()).thenReturn(List.of(planet));

        List<PlanetDTO> planetDTOList = planetService.findAll();
        assertFalse(planetDTOList.isEmpty());
        assertEquals(PLANET_NAME, planetDTOList.get(0).getPlanetName());
        verify(planetRepo, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        Planet planet = new Planet();
        planet.setPlanetId(PLANET_ID);
        planet.setPlanetName(PLANET_NAME);

        when(planetRepo.findById(PLANET_ID)).thenReturn(Optional.of(planet));

        PlanetDTO planetDTO = planetService.findById(PLANET_ID);

        assertNotNull(planetDTO);
        assertEquals(PLANET_NAME, planetDTO.getPlanetName());
    }

    @Test
    public void testFindByIdThrowsExceptionWhenPlanetNotFound() {
        when(planetRepo.findById(PLANET_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(PlanetNotFoundException.class, () -> {
            planetService.findById(PLANET_ID);
        });

        assertEquals("Planet not found", exception.getMessage());
    }

    @Test
    public void testIsOccupied() {
        Coordinates coordinates = new Coordinates(X, Y);
        PlanetDTO planetDTO = new PlanetDTO();
        Planet planet = new Planet();
        planetDTO.setPlanetId(PLANET_ID);
        planetDTO.setPlanetName(PLANET_NAME);

        when(planetRepo.findById(1L)).thenReturn(Optional.of(planet));
        when(coordinateService.isOccupied(coordinates, planet)).thenReturn(true);

        boolean isOccupied = planetService.isOccupied(1L, coordinates);

        assertTrue(isOccupied);
    }

    @Test
    public void testAddRover() {
        Planet planet = new Planet();
        planet.setPlanetId(PLANET_ID);
        planet.setPlanetName(PLANET_NAME);

        Rover rover = new Rover();
        rover.setRoverId(ROVER_ID);
        rover.setCoordinates(new Coordinates(X, Y));
        rover.setDirection(DIRECTION);

        RoverDTO roverDTO = new RoverDTO();
        roverDTO.setRoverId(ROVER_ID);
        roverDTO.setCoordinates(new Coordinates(X, Y));
        roverDTO.setDirection(DIRECTION);

        when(planetRepo.findById(PLANET_ID)).thenReturn(Optional.of(planet));
        when(roverRepo.findById(ROVER_ID)).thenReturn(Optional.of(rover));
        when(coordinateService.isOccupied(any(Coordinates.class), any(Planet.class))).thenReturn(false);
        when(planetRepo.save(any(Planet.class))).thenReturn(planet);

        PlanetDTO updatedPlanetDTO = planetService.addRover(PLANET_ID, roverDTO);

        assertNotNull(updatedPlanetDTO);
        assertEquals(1, updatedPlanetDTO.getRovers().size());
    }

    @Test
    public void testAddRoverThrowsExceptionWhenPlanetNotFound() {
        RoverDTO roverDTO = new RoverDTO();
        roverDTO.setRoverId(ROVER_ID);

        when(planetRepo.findById(PLANET_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(PlanetNotFoundException.class, () -> {
            planetService.addRover(PLANET_ID, roverDTO);
        });

        assertEquals("Planet not found", exception.getMessage());
    }

    @Test
    public void testAddRoverThrowsExceptionWhenRoverNotFound() {
        Planet planet = new Planet();
        planet.setPlanetId(PLANET_ID);

        RoverDTO roverDTO = new RoverDTO();
        roverDTO.setRoverId(ROVER_ID);

        when(planetRepo.findById(PLANET_ID)).thenReturn(Optional.of(planet));
        when(roverRepo.findById(ROVER_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RoverNotFoundException.class, () -> {
            planetService.addRover(PLANET_ID, roverDTO);
        });

        assertEquals("Rover not found", exception.getMessage());
    }

    @Test
    public void testAddRoverThrowsExceptionWhenCoordinatesOrDirectionAreNull() {
        Planet planet = new Planet();
        planet.setPlanetId(PLANET_ID);
        planet.setPlanetName(PLANET_NAME);

        Rover rover = new Rover();
        rover.setRoverId(ROVER_ID);

        RoverDTO roverDTO = new RoverDTO();
        roverDTO.setRoverId(ROVER_ID);

        when(planetRepo.findById(PLANET_ID)).thenReturn(Optional.of(planet));
        when(roverRepo.findById(ROVER_ID)).thenReturn(Optional.of(rover));

        Exception exception = assertThrows(InvalidCommandException.class, () -> {
            planetService.addRover(1L, roverDTO);
        });

        assertEquals("Coordinates and directions must not be null.", exception.getMessage());
    }

    @Test
    public void testAddRoverThrowsExceptionWhenCoordinatesAreNegative() {
        Planet planet = new Planet();
        planet.setPlanetId(PLANET_ID);
        planet.setPlanetName(PLANET_NAME);

        Rover rover = new Rover();
        rover.setRoverId(ROVER_ID);
        rover.setCoordinates(new Coordinates(-1, -1));
        rover.setDirection(DIRECTION);

        RoverDTO roverDTO = new RoverDTO();
        roverDTO.setRoverId(ROVER_ID);
        roverDTO.setCoordinates(new Coordinates(-1, -1));
        roverDTO.setDirection(DIRECTION);

        when(planetRepo.findById(PLANET_ID)).thenReturn(Optional.of(planet));
        when(roverRepo.findById(ROVER_ID)).thenReturn(Optional.of(rover));

        Exception exception = Assertions.assertThrows(InvalidCommandException.class, () -> {
            planetService.addRover(PLANET_ID, roverDTO);
        });

        assertEquals("Coordinates must not be negative.", exception.getMessage());
    }

    @Test
    public void testAddRoverThrowsExceptionWhenPlanetIsFull() {
        Planet planet = new Planet();
        planet.setPlanetId(PLANET_ID);
        planet.setPlanetName(PLANET_NAME);
        planet.setRovers(List.of(new Rover(), new Rover(), new Rover(), new Rover(), new Rover()));

        Rover rover = new Rover();
        rover.setRoverId(ROVER_ID);
        rover.setCoordinates(new Coordinates(X, Y));
        rover.setDirection(DIRECTION);

        RoverDTO roverDTO = new RoverDTO();
        roverDTO.setRoverId(ROVER_ID);
        roverDTO.setCoordinates(new Coordinates(X, Y));
        roverDTO.setDirection(DIRECTION);

        when(planetRepo.findById(PLANET_ID)).thenReturn(Optional.of(planet));
        when(roverRepo.findById(ROVER_ID)).thenReturn(Optional.of(rover));

        Exception exception = assertThrows(PlanetFullException.class, () -> {
            planetService.addRover(1L, roverDTO);
        });

        assertEquals("Planet is full", exception.getMessage());
    }

    @Test
    public void testAddRoverThrowsExceptionWhenCoordinateIsOccupied() {
        Planet planet = new Planet();
        planet.setPlanetId(PLANET_ID);
        planet.setPlanetName(PLANET_NAME);

        Rover rover = new Rover();
        rover.setRoverId(ROVER_ID);
        rover.setCoordinates(new Coordinates(X, Y));
        rover.setDirection(DIRECTION);

        RoverDTO roverDTO = new RoverDTO();
        roverDTO.setRoverId(ROVER_ID);
        roverDTO.setCoordinates(new Coordinates(X, Y));
        roverDTO.setDirection(DIRECTION);

        when(planetRepo.findById(PLANET_ID)).thenReturn(Optional.of(planet));
        when(roverRepo.findById(ROVER_ID)).thenReturn(Optional.of(rover));
        when(coordinateService.isOccupied(any(Coordinates.class), any(Planet.class))).thenReturn(true);

        Exception exception = assertThrows(CoordinateOccupiedException.class, () -> {
            planetService.addRover(PLANET_ID, roverDTO);
        });

        assertEquals("Coordinate is already occupied", exception.getMessage());
    }

    @Test
    public void testAddRoverThrowsExceptionWhenRoverIsAlreadyAddedToPlanet() {
        Planet planet = new Planet();
        planet.setPlanetId(PLANET_ID);
        planet.setPlanetName(PLANET_NAME);

        Rover rover = new Rover();
        rover.setRoverId(ROVER_ID);
        rover.setCoordinates(new Coordinates(X, Y));
        rover.setDirection(DIRECTION);

        planet.getRovers().add(rover);

        RoverDTO roverDTO = new RoverDTO();
        roverDTO.setRoverId(ROVER_ID);
        roverDTO.setCoordinates(new Coordinates(X, Y));
        roverDTO.setDirection(DIRECTION);

        when(planetRepo.findById(PLANET_ID)).thenReturn(Optional.of(planet));
        when(roverRepo.findById(ROVER_ID)).thenReturn(Optional.of(rover));

        Exception exception = assertThrows(RoverUnavailableException.class, () -> {
            planetService.addRover(PLANET_ID, roverDTO);
        });

        assertEquals("Rover is already added to this planet", exception.getMessage());
    }

    @Test
    public void testAddRoverThrowsExceptionWhenRoverIsAssociatedWithAnotherPlanet() {
        Planet planet = new Planet();
        planet.setPlanetId(PLANET_ID);
        planet.setPlanetName(PLANET_NAME);

        Rover rover = new Rover();
        rover.setRoverId(ROVER_ID);
        rover.setCoordinates(new Coordinates(X, Y));
        rover.setDirection(DIRECTION);
        rover.setPlanet(new Planet());

        RoverDTO roverDTO = new RoverDTO();
        roverDTO.setRoverId(ROVER_ID);
        roverDTO.setCoordinates(new Coordinates(X, Y));
        roverDTO.setDirection(DIRECTION);

        when(planetRepo.findById(PLANET_ID)).thenReturn(Optional.of(planet));
        when(roverRepo.findById(ROVER_ID)).thenReturn(Optional.of(rover));

        Exception exception = assertThrows(RoverUnavailableException.class, () -> {
            planetService.addRover(PLANET_ID, roverDTO);
        });

        assertEquals("Rover is already associated with another planet", exception.getMessage());
    }


}