package com.br.eliza.spaceprobe.service.planet;

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
public class PlanetServiceImpl implements PlanetService{

    private final PlanetRepository planetRepo;
    private final RoverRepository roverRepo;
    private final CoordinateService coordinateService;

    public PlanetServiceImpl(PlanetRepository planetRepo, RoverRepository roverRepo, CoordinateService coordinateService) {
        this.planetRepo = planetRepo;
        this.roverRepo = roverRepo;
        this.coordinateService = coordinateService;
    }

    @Transactional
    @Override
    public Planet save(Planet planet) {
        return planetRepo.save(planet);
    }

    @Override
    public List<Planet> findAll() {
        return planetRepo.findAll();
    }

    @Override
    public Planet findById(Long id) {
        return planetRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Planet not found"));
    }

    @Transactional
    @Override
    public Planet addRover(Long planetId, Long roverId) {
        Planet planet = planetRepo.findById(planetId)
                .orElseThrow(() -> new RuntimeException("Planet not found"));

        Rover rover = roverRepo.findById(roverId)
                .orElseThrow(() -> new RuntimeException("Rover not found"));

        if (planet.getRovers().size() >= 5) {
            throw new RuntimeException("Planet is full");
        }
        if (isOccupied(planetId, rover.getCoordinates())) {
            throw new RuntimeException("Coordinate is already occupied");
        }

        if (planet.getRovers().stream().anyMatch(r -> r.getRoverId().equals(rover.getRoverId()))) {
            throw new RuntimeException("Rover is already added to this planet");
        }

        if (rover.getPlanet() != null) {
            throw new RuntimeException("Rover is already associated with another planet");
        }

        rover.setPlanet(planet);
        planet.getRovers().add(rover);
        return planetRepo.save(planet);

    }

    @Override
    public boolean isOccupied(Long planetId, Coordinates coordinates) {
        Planet planet = planetRepo.findById(planetId)
                .orElseThrow(() -> new RuntimeException("Planet not found"));
        return coordinateService.isOccupied(coordinates, planet);
    }

    //fake delete
    @Transactional
    @Override
    public void removeRover(Long planetId, Long roverId) {
        Planet planet = planetRepo.findById(planetId)
                .orElseThrow(() -> new RuntimeException("Planet not found"));

        Rover rover = roverRepo.findById(roverId)
                .orElseThrow(() -> new RuntimeException("Rover not found"));

        if (!planet.getRovers().contains(rover)) {
            throw new RuntimeException("Rover not found in the planet");
        }

        planet.getRovers().remove(rover);
        rover.setPlanet(null);
        planetRepo.save(planet);
        roverRepo.save(rover);
    }

}