package com.br.eliza.spaceprobe.service.planet;

import com.br.eliza.spaceprobe.dto.PlanetDTO;
import com.br.eliza.spaceprobe.model.Coordinates;

import java.util.List;

public interface PlanetService {
    List<PlanetDTO> findAll();
    PlanetDTO save(PlanetDTO planet);
    PlanetDTO findById(Long id);
    PlanetDTO addRover(Long planetId, Long roverId);
    boolean isOccupied(Long planetId, Coordinates coordinates);

}
