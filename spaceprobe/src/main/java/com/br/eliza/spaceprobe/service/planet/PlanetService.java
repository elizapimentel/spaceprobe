package com.br.eliza.spaceprobe.service.planet;

import com.br.eliza.spaceprobe.model.Coordinates;
import com.br.eliza.spaceprobe.model.Planet;
import com.br.eliza.spaceprobe.model.Rover;

import java.util.List;

public interface PlanetService {
    List<Planet> findAll();
    Planet save(Planet planet);
    Planet findById(Long id);
    Planet addRover(Long planetId, Rover rover);
    boolean isOccupied(Long planetId, Coordinates coordinates);

}
