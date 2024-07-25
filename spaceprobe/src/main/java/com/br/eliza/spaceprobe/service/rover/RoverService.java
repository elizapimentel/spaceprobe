package com.br.eliza.spaceprobe.service.rover;

import com.br.eliza.spaceprobe.dto.CommandDTO;
import com.br.eliza.spaceprobe.dto.RoverDTO;

import java.util.List;

public interface RoverService {
    List<RoverDTO> findAll();
    RoverDTO save(RoverDTO rover);
    RoverDTO moveRover(CommandDTO commandDTO);
    RoverDTO findById(Long id);
    RoverDTO updatePlanet(Long roverId, Long planetId);
    RoverDTO turnOnOff(Long roverId);

}
