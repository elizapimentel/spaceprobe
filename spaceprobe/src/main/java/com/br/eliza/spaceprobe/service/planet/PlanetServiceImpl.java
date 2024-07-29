package com.br.eliza.spaceprobe.service.planet;

import com.br.eliza.spaceprobe.dto.PlanetDTO;
import com.br.eliza.spaceprobe.dto.RoverDTO;
import com.br.eliza.spaceprobe.exceptions.*;
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
    public PlanetDTO save(PlanetDTO planetDTO) {
        Planet planet = planetDTO.convertDtoToEntity();
        if (planet.getPlanetName()==null || planet.getPlanetName().isBlank()) {
            throw new RuntimeException("Planet name must not be informed.");
        }
        Planet savedPlanet = planetRepo.save(planet);
        return savedPlanet.convertEntityToDto();
    }

    @Override
    public List<PlanetDTO> findAll() {
        List<Planet> planets = planetRepo.findAll();
        return planets.stream()
                .map(Planet::convertEntityToDto)
                .toList();
    }

    @Override
    public PlanetDTO findById(Long id) {
        Planet planet = planetRepo.findById(id)
                .orElseThrow(() ->  new PlanetNotFoundException("Planet not found"));
        return planet.convertEntityToDto();
    }

    @Override
    public boolean isOccupied(Long planetId, Coordinates coordinates) {
        PlanetDTO planetDTO = findById(planetId);
        Planet planet = planetDTO.convertDtoToEntity();
        return coordinateService.isOccupied(coordinates, planet);
    }

    @Transactional
    @Override
    public PlanetDTO addRover(Long planetId, RoverDTO roverDTO) {
        Planet planet = planetRepo.findById(planetId)
                .orElseThrow(() -> new PlanetNotFoundException("Planet not found"));

        Rover rover = roverRepo.findById(roverDTO.getRoverId())
                .orElseThrow(() -> new RoverNotFoundException("Rover not found"));

        if (roverDTO.getCoordinates() != null) {
            rover.setCoordinates(roverDTO.getCoordinates());
        }
        if (roverDTO.getDirection() != null) {
            rover.setDirection(roverDTO.getDirection());
        }

        if (rover.getCoordinates() == null || rover.getDirection() == null) {
            throw new InvalidCommandException("Coordinates and directions must not be null.");
        }

        int roverX = rover.getCoordinates().getX();
        int roverY = rover.getCoordinates().getY();

        if (roverX < 0 || roverY < 0) {
            throw new InvalidCommandException("Coordinates must not be negative.");
        }

        if (planet.getRovers().size() >= 5) {
            throw new PlanetFullException("Planet is full");
        }

        if (isOccupied(planetId, rover.getCoordinates())) {
            throw new CoordinateOccupiedException("Coordinate is already occupied");
        }

        if (planet.getRovers().stream().anyMatch(r -> r.getRoverId().equals(rover.getRoverId()))) {
            throw new RoverUnavailableException("Rover is already added to this planet");
        }

        if (rover.getPlanet() != null) {
            throw new RoverUnavailableException("Rover is already associated with another planet");
        }

        if (roverDTO.getIsOn() != null) {
            rover.setIsOn(roverDTO.getIsOn());
        }

        rover.setPlanet(planet);
        planet.getRovers().add(rover);
        planetRepo.save(planet);

        return planet.convertEntityToDto();
    }


}