package com.br.eliza.spaceprobe.service.planet;

import com.br.eliza.spaceprobe.model.Coordinates;
import com.br.eliza.spaceprobe.model.Planet;

import java.util.List;

public interface PlanetService {
    List<Planet> findAll();
    Planet save(Planet planet);
    Planet findById(Long id);
    Planet addRover(Long planetId, Long roverId);
    boolean isOccupied(Long planetId, Coordinates coordinates);
    void removeRover(Long planetId, Long roverId);

}
