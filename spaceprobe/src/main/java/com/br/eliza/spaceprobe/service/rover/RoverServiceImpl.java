package com.br.eliza.spaceprobe.service.rover;

import com.br.eliza.spaceprobe.dto.CommandDTO;
import com.br.eliza.spaceprobe.dto.RoverDTO;
import com.br.eliza.spaceprobe.enums.Direction;
import com.br.eliza.spaceprobe.model.Coordinates;
import com.br.eliza.spaceprobe.model.Planet;
import com.br.eliza.spaceprobe.model.Rover;
import com.br.eliza.spaceprobe.repository.PlanetRepository;
import com.br.eliza.spaceprobe.repository.RoverRepository;
import com.br.eliza.spaceprobe.service.coordinate.CoordinateService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoverServiceImpl implements RoverService {

    private final RoverRepository roverRepo;
    private final PlanetRepository planetRepo;
    private final CoordinateService coordinateService;
    private final ModelMapper modelMapper;

    public RoverServiceImpl(RoverRepository roverRepo, PlanetRepository planetRepo, CoordinateService coordinateService, ModelMapper modelMapper) {
        this.roverRepo = roverRepo;
        this.planetRepo = planetRepo;
        this.coordinateService = coordinateService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public RoverDTO save(RoverDTO roverDTO) {
        Rover rover = roverDTO.convertDtoToEntity();
        Rover savedRover = roverRepo.save(rover);
        return savedRover.convertEntityToDto();
    }

    @Override
    public List<RoverDTO> findAll() {
        List<Rover> rovers = roverRepo.findAll();
        return rovers.stream()
                .map(Rover::convertEntityToDto)
                .toList();
    }

    @Override
    public RoverDTO findById(Long id) {
        Rover rover = roverRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Rover not found"));
        if (rover.getPlanet() != null) {
            System.out.println("Rover is allocated on planet: " + rover.getPlanet().getPlanetName());
        } else {
            System.out.println("Rover is not allocated on any planet.");
        }
        return rover.convertEntityToDto();
    }

    @Transactional
    @Override
    public RoverDTO moveRover(CommandDTO roverCommandDTO) {
        Rover rover = findById(roverCommandDTO.getRoverId()).convertDtoToEntity();
        Planet planet = rover.getPlanet();
        Coordinates currentCoordinates = rover.getCoordinates();
        Direction currentDirection = rover.getDirection();

        for (char command : roverCommandDTO.getCommands()) {
            switch (command) {
                case 'M':
                    Coordinates newCoordinates = coordinateService.calculateNewCoordinates(currentCoordinates, currentDirection);
                    if (coordinateService.isOccupied(newCoordinates, planet)) {
                        throw new RuntimeException("Move results in collision with another rover");
                    }
                    currentCoordinates = newCoordinates;
                    break;
                case 'L':
                    currentDirection = coordinateService.turnLeft(currentDirection);
                    break;
                case 'R':
                    currentDirection = coordinateService.turnRight(currentDirection);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid command: " + command);
            }
        }

        rover.setCoordinates(currentCoordinates);
        rover.setDirection(currentDirection);
        Rover updatedRover = roverRepo.save(rover);
        planetRepo.save(planet);

        return modelMapper.map(updatedRover, RoverDTO.class);
    }

    @Override
    public RoverDTO updatePlanet(Long roverId, Long planetId) {
        Rover rover = findById(roverId).convertDtoToEntity();

        if (rover.getPlanet() != null) {
            rover.getPlanet().getRovers().remove(rover);
            planetRepo.save(rover.getPlanet());
        }

        if (planetId != null) {
            Planet newPlanet = planetRepo.findById(planetId)
                    .orElseThrow(() -> new RuntimeException("Planet not found"));
            if (coordinateService.isOccupied(rover.getCoordinates(), newPlanet)) {
                throw new RuntimeException("Coordinate is already occupied on the new planet. Try to relocate the rover first.");
            }
            rover.setPlanet(newPlanet);
            newPlanet.getRovers().add(rover);
            planetRepo.save(newPlanet);
        } else {
            rover.setPlanet(null);
        }

        Rover updatedRover = roverRepo.save(rover);
        return updatedRover.convertEntityToDto();
    }

    @Override
    public RoverDTO turnOnOff(Long roverId) {
        Rover rover = findById(roverId).convertDtoToEntity();
        rover.setOn(!rover.isOn()); // Alterna o estado do rover
        Rover updatedRover = roverRepo.save(rover);
        return updatedRover.convertEntityToDto();
    }

}
