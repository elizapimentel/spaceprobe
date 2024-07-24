package com.br.eliza.spaceprobe.service.planet;

import com.br.eliza.spaceprobe.model.Coordinates;
import com.br.eliza.spaceprobe.model.Planet;
import com.br.eliza.spaceprobe.model.Rover;
import com.br.eliza.spaceprobe.repository.PlanetRepository;
import com.br.eliza.spaceprobe.repository.RoverRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanetServiceImpl implements PlanetService{

    private final PlanetRepository planetRepo;
    private final RoverRepository roverRepo;

    public PlanetServiceImpl(PlanetRepository planetRepo, RoverRepository roverRepo) {
        this.planetRepo = planetRepo;
        this.roverRepo = roverRepo;
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
    public Planet addRover(Long planetId, Rover rover) {
        Planet planet = findById(planetId);

        if (planet.getRovers().size() >= 5) {
            throw new RuntimeException("Planet is full");
        }
        if (isOccupied(planetId, rover.getCoordinates())) {
            throw new RuntimeException("Coordinate is already occupied");
        }

        rover.setPlanet(planet);
        planet.getRovers().add(rover);
        return planetRepo.save(planet);

    }

    @Override
    public boolean isOccupied(Long planetId, Coordinates coordinates) {
        Planet planet = findById(planetId);
        return planet.getRovers().stream()
                .anyMatch(rover -> rover.getCoordinates().equals(coordinates));
    }

}