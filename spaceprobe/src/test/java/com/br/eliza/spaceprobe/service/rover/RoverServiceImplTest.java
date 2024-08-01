package com.br.eliza.spaceprobe.service.rover;

import static com.br.eliza.spaceprobe.common.Constants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.br.eliza.spaceprobe.dto.CommandDTO;
import com.br.eliza.spaceprobe.dto.PlanetDTO;
import com.br.eliza.spaceprobe.dto.RoverDTO;
import com.br.eliza.spaceprobe.enums.Direction;
import com.br.eliza.spaceprobe.exceptions.CoordinateOccupiedException;
import com.br.eliza.spaceprobe.exceptions.InvalidCommandException;
import com.br.eliza.spaceprobe.exceptions.PlanetNotFoundException;
import com.br.eliza.spaceprobe.exceptions.RoverNotFoundException;
import com.br.eliza.spaceprobe.model.Coordinates;
import com.br.eliza.spaceprobe.model.Planet;
import com.br.eliza.spaceprobe.model.Rover;
import com.br.eliza.spaceprobe.repository.PlanetRepository;
import com.br.eliza.spaceprobe.repository.RoverRepository;
import com.br.eliza.spaceprobe.service.coordinate.CoordinateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RoverServiceImplTest {

    @Mock
    private RoverRepository roverRepo;

    @Mock
    private PlanetRepository planetRepo;

    @Mock
    private CoordinateService coordinateService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private RoverServiceImpl roverService;

    private Rover rover;
    private RoverDTO roverDTO;
    private Planet planet;
    private PlanetDTO planetDTO;
    private Coordinates coordinates;
    private CommandDTO commandDTO;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        buildRover();
    }

    @Test
    public void mustSaveRover() {
        when(modelMapper.map(roverDTO, Rover.class)).thenReturn(rover);
        when(roverRepo.save(any(Rover.class))).thenReturn(rover);

        Rover savedRover = roverRepo.save(rover);
        when(modelMapper.map(savedRover, RoverDTO.class)).thenReturn(roverDTO);

        assertThat(roverDTO).isNotNull();
        assertThat(roverDTO.getRoverId()).isEqualTo(1L);
        verify(roverRepo, times(1)).save(any(Rover.class));
    }

    @Test
    void mustThrowExceptionWhenCoordinatesOrDirectionIsNull() {
        roverDTO.setCoordinates(null);
        assertThrows(InvalidCommandException.class, () -> roverService.save(roverDTO));

        roverDTO.setCoordinates(new Coordinates(1, 1));
        roverDTO.setDirection(null);
        assertThrows(InvalidCommandException.class, () -> roverService.save(roverDTO));
    }

    @Test
    void mustThrowExceptionWhenCoordinatesAreInvalid() {
        roverDTO.setCoordinates(new Coordinates(0, 1));
        assertThrows(InvalidCommandException.class, () -> roverService.save(roverDTO));

        roverDTO.setCoordinates(new Coordinates(1, -1));
        assertThrows(InvalidCommandException.class, () -> roverService.save(roverDTO));
    }

    @Test
    void mustFindAllRovers() {
        when(roverRepo.findAll()).thenReturn(List.of(rover));

        when(modelMapper.map(rover, RoverDTO.class)).thenReturn(roverDTO);
        List<RoverDTO> rovers = roverService.findAll();

        assertThat(rovers).isNotNull();
    }

    @Test
    void mustFindRoverById() {
        when(roverRepo.findById(anyLong())).thenReturn(Optional.of(rover));

        Rover roverId = roverRepo.findById(ROVER_ID).orElseThrow();
        when(modelMapper.map(roverId, RoverDTO.class)).thenReturn(roverDTO);

        assertThat(roverDTO).isNotNull();
        assertThat(roverDTO.getRoverId()).isEqualTo(ROVER_ID);
        verify(roverRepo, times(1)).findById(ROVER_ID);
    }

    @Test
    void mustThrowRoverNotFoundExceptionWhenRoverNotFound() {
        when(roverRepo.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RoverNotFoundException.class, () -> roverService.findById(null));

    }

    @Test
    void shouldMoveRoverAndUpdateDirection() {

        Coordinates newCoordinates = new Coordinates(2, 2);
        commandDTO.setRoverId(1L);
        commandDTO.setCommands(Arrays.asList('M', 'L', 'M', 'R'));

        when(roverRepo.findById(ROVER_ID)).thenReturn(Optional.of(rover));

        Rover updatedRover = new Rover();
        updatedRover.setRoverId(ROVER_ID);
        updatedRover.setCoordinates(newCoordinates);
        updatedRover.setDirection(Direction.EAST);
        updatedRover.setPlanet(planet);
        updatedRover.setIsOn(true);

        when(coordinateService.calculateNewCoordinates(any(Coordinates.class), any(Direction.class)))
                .thenReturn(newCoordinates);
        when(coordinateService.isOccupied(any(Coordinates.class), any(Planet.class))).thenReturn(false);
        when(coordinateService.turnLeft(any(Direction.class))).thenReturn(Direction.WEST);
        when(coordinateService.turnRight(any(Direction.class))).thenReturn(Direction.EAST);
        when(roverRepo.save(any(Rover.class))).thenReturn(updatedRover);
        when(planetRepo.save(any(Planet.class))).thenReturn(planet);

        RoverDTO updatedRoverDTO = new RoverDTO();
        updatedRoverDTO.setRoverId(ROVER_ID);
        updatedRoverDTO.setCoordinates(newCoordinates);
        updatedRoverDTO.setDirection(Direction.EAST);
        updatedRoverDTO.setPlanetDTO(planetDTO);
        updatedRoverDTO.setIsOn(true);

        when(modelMapper.map(any(Rover.class), eq(RoverDTO.class))).thenReturn(updatedRoverDTO);


        assertEquals(newCoordinates, updatedRover.getCoordinates());
        assertEquals(Direction.EAST, updatedRover.getDirection());
        assertNotEquals(newCoordinates, coordinates);

    }

    @Test
    public void testMoveRoverThrowsExceptionWhenRoverNotFound() {
        CommandDTO commandDTO = new CommandDTO();
        commandDTO.setRoverId(ROVER_ID);
        commandDTO.setCommands(List.of('M'));

        when(roverRepo.findById(ROVER_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RoverNotFoundException.class, () -> {
            roverService.moveRover(commandDTO);
        });

        assertEquals(ROVER_ID_NOT_FOUND_MESSAGE + ROVER_ID, exception.getMessage());
    }

    @Test
    void testUpdatePlanet() {
        when(modelMapper.map(roverDTO, Rover.class)).thenReturn(rover);
        when(roverRepo.findById(anyLong())).thenReturn(Optional.of(rover));

        Planet newPlanet = new Planet();
        newPlanet.setPlanetId(2L);
        newPlanet.setPlanetName("Earth");
        rover.setPlanet(newPlanet);

        when(planetRepo.findById(anyLong())).thenReturn(Optional.of(newPlanet));
        when(coordinateService.isOccupied(COORDINATES, newPlanet)).thenReturn(false);
        when(roverRepo.save(rover)).thenReturn(rover);
        when(planetRepo.save(newPlanet)).thenReturn(newPlanet);

        Rover updatedRover = roverRepo.save(rover);
        when(modelMapper.map(updatedRover, RoverDTO.class)).thenReturn(roverDTO);

        assertNotNull(updatedRover);
        assertEquals(2L, updatedRover.getPlanet().getPlanetId());
        assertEquals("Earth", updatedRover.getPlanet().getPlanetName());

    }

    @Test
    public void testUpdatePlanetPlanetNotFound() {
        when(modelMapper.map(roverDTO, Rover.class)).thenReturn(rover);
        when(roverRepo.findById(anyLong())).thenReturn(Optional.of(rover));

        when(planetRepo.findById(anyLong())).thenReturn(Optional.empty())
                .thenThrow(new PlanetNotFoundException(PLANET_NOT_FOUND_MESSAGE));

    }


    @Test
    public void testMoveRoverThrowsNullPointerExceptionWhenPlanetIsNull() {
        when(modelMapper.map(roverDTO, Rover.class)).thenReturn(rover);
        when(roverRepo.save(any(Rover.class))).thenReturn(rover);

        rover.setPlanet(null);

        when(roverRepo.findById(ROVER_ID)).thenReturn(Optional.of(rover))
                .thenThrow(new NullPointerException(PLANET_NOT_FOUND_MESSAGE));
    }


    @Test
    public void testUpdatePlanetCoordinateOccupied() {
        when(coordinateService.isOccupied(any(Coordinates.class), any(Planet.class))).thenReturn(true)
                .thenThrow(new CoordinateOccupiedException(COORDINATE_ALREADY_OCCUPIED_MESSAGE));
    }


    @Test
    void mustTurnOnOffRover() {
        when(modelMapper.map(roverDTO, Rover.class)).thenReturn(rover);
        when(roverRepo.findById(anyLong())).thenReturn(Optional.of(rover));

        rover.setIsOn(true);

        Rover updatedRover = roverRepo.save(rover);
        when(modelMapper.map(updatedRover, RoverDTO.class)).thenReturn(roverDTO);

        assertThat(roverDTO).isNotNull();
        assertTrue(roverDTO.getIsOn());
        assertTrue(rover.getIsOn());
        verify(roverRepo, times(1)).save(rover);
    }

    private void buildRover() {
        planet = new Planet(PLANET_ID, PLANET_NAME, WIDTH, HEIGHT, null);

        rover = new Rover();
        rover.setRoverId(ROVER_ID);
        rover.setCoordinates(new Coordinates(X, Y));
        rover.setDirection(Direction.NORTH);
        rover.setPlanet(planet);
        rover.setIsOn(true);

        planetDTO = new PlanetDTO();
        planetDTO.setPlanetId(PLANET_ID);
        planetDTO.setPlanetName(PLANET_NAME);
        planetDTO.setWidth(WIDTH);
        planetDTO.setHeight(HEIGHT);

        roverDTO = new RoverDTO();
        roverDTO.setRoverId(ROVER_ID);
        roverDTO.setCoordinates(new Coordinates(X, Y));
        roverDTO.setDirection(Direction.NORTH);
        roverDTO.setPlanetDTO(new PlanetDTO());
        roverDTO.getPlanetDTO().setPlanetId(PLANET_ID);
        roverDTO.getPlanetDTO().setPlanetName(PLANET_NAME);
        roverDTO.getPlanetDTO().setWidth(WIDTH);
        roverDTO.getPlanetDTO().setHeight(HEIGHT);
        roverDTO.setIsOn(true);

        coordinates = new Coordinates(X, Y);

        commandDTO = new CommandDTO();


    }
}


