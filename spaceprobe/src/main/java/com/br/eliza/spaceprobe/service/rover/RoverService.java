package com.br.eliza.spaceprobe.service.rover;

import com.br.eliza.spaceprobe.dto.RoverCommandDTO;
import com.br.eliza.spaceprobe.model.Rover;

import java.util.List;

public interface RoverService {
    List<Rover> findAll();
    Rover save(Rover rover);
    Rover moveRover(RoverCommandDTO commandDTO);
    Rover findById(Long id);
    Rover updatePlanet(Long roverId, Long planetId);

}
