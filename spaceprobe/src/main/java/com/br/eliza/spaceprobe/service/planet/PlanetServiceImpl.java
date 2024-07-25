package com.br.eliza.spaceprobe.service.planet;

import com.br.eliza.spaceprobe.dto.PlanetDTO;
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
public class PlanetServiceImpl implements PlanetService{

    private final PlanetRepository planetRepo;
    private final RoverRepository roverRepo;
    private final CoordinateService coordinateService;
    private final ModelMapper modelMapper;

    public PlanetServiceImpl(PlanetRepository planetRepo, RoverRepository roverRepo, CoordinateService coordinateService, ModelMapper modelMapper) {
        this.planetRepo = planetRepo;
        this.roverRepo = roverRepo;
        this.coordinateService = coordinateService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public PlanetDTO save(PlanetDTO planetDTO) {
        Planet planet = planetDTO.convertDtoToEntity();
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
                .orElseThrow(() -> new RuntimeException("Planet not found"));
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
    public PlanetDTO addRover(Long planetId, Long roverId) {
        PlanetDTO planetDTO = findById(planetId);
        Planet planet = planetDTO.convertDtoToEntity();

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
        planetRepo.save(planet);

        return planet.convertEntityToDto();

    }


}