package com.br.eliza.spaceprobe.service.rover;

import com.br.eliza.spaceprobe.dto.RoverCommandDTO;
import com.br.eliza.spaceprobe.enums.Direction;
import com.br.eliza.spaceprobe.model.Coordinates;
import com.br.eliza.spaceprobe.model.Planet;
import com.br.eliza.spaceprobe.model.Rover;
import com.br.eliza.spaceprobe.repository.PlanetRepository;
import com.br.eliza.spaceprobe.repository.RoverRepository;
import com.br.eliza.spaceprobe.service.coordinate.CoordinateService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoverServiceImpl implements RoverService {

    private final RoverRepository roverRepo;
    private final PlanetRepository planetRepo;
    private final CoordinateService coordinateService;

    public RoverServiceImpl(RoverRepository roverRepo, PlanetRepository planetRepo, CoordinateService coordinateService) {
        this.roverRepo = roverRepo;
        this.planetRepo = planetRepo;
        this.coordinateService = coordinateService;
    }

    @Transactional
    @Override
    public Rover save(Rover rover) {
        return roverRepo.save(rover);
    }

    @Override
    public List<Rover> findAll() {
        return roverRepo.findAll();
    }

    @Transactional
    @Override
    public Rover moveRover(RoverCommandDTO roverCommandDTO) {
        Rover rover = findById(roverCommandDTO.getRoverId());
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
        roverRepo.save(rover);
        planetRepo.save(planet);

        return rover;
    }

    @Override
    public Rover findById(Long id) {
        Rover rover = roverRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Rover not found"));
        if (rover.getPlanet() != null) {
            System.out.println("Rover is allocated on planet: " + rover.getPlanet().getPlanetName());
        } else {
            System.out.println("Rover is not allocated on any planet.");
        }
        return rover;
    }

    @Override
    public Rover updatePlanet(Long roverId, Long planetId) {
        Rover rover = findById(roverId);

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

        return roverRepo.save(rover);
    }

    @Override
    public Rover turnOnOff(Long roverId) {
        Rover rover = findById(roverId);
        rover.setOn(!rover.isOn()); // Alterna o estado do rover
        return roverRepo.save(rover);
    }

}
