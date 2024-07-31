package com.br.eliza.spaceprobe.controller.rover;

import com.br.eliza.spaceprobe.controller.planet.PlanetController;
import com.br.eliza.spaceprobe.dto.CommandDTO;
import com.br.eliza.spaceprobe.dto.RoverDTO;
import com.br.eliza.spaceprobe.exceptions.RoverNotFoundException;
import com.br.eliza.spaceprobe.service.rover.RoverService;
import com.br.eliza.spaceprobe.util.LinkUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/v2/rovers")
@Validated
public class RoverController {

    private final RoverService service;
    private final LinkUtil linkUtil;
    private static final Logger logger = Logger.getLogger(PlanetController.class.getName());


    public RoverController(RoverService service, LinkUtil linkUtil) {
        this.service = service;
        this.linkUtil = linkUtil;

    }

    @PostMapping("/add")
    public ResponseEntity<RoverDTO> save(@Valid @RequestBody RoverDTO rover) {
        RoverDTO savedRover = service.save(rover);
        linkUtil.createSelfLinkInRover(savedRover);
        return new ResponseEntity<>(savedRover, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<RoverDTO>> findAll() {
        List<RoverDTO> rovers = service.findAll();
        List<RoverDTO> roversDTO = new ArrayList<>();

        rovers.forEach(linkUtil::createSelfLinkInCollectionsToRover);

        return new ResponseEntity<>(roversDTO, HttpStatus.OK);
    }

    @PostMapping("/move")
    public ResponseEntity<RoverDTO> moveRover(@Valid @RequestBody CommandDTO commandDTO) {
        RoverDTO updatedRover = service.moveRover(commandDTO);
        linkUtil.createSelfLinkInRover(updatedRover);
        return new ResponseEntity<>(updatedRover, HttpStatus.OK);
    }

    @GetMapping("/{roverId}")
    public ResponseEntity<RoverDTO> findById(@PathVariable Long roverId) {
        try {
            RoverDTO rover = service.findById(roverId);
            linkUtil.createSelfLinkInRover(rover);
            return new ResponseEntity<>(rover, HttpStatus.OK);
        } catch (RoverNotFoundException e) {
            logger.log(Level.SEVERE, e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }

    @PutMapping("/{roverId}/planet/{newPlanetId}")
    public ResponseEntity<RoverDTO> updateRoverPlanet(
            @PathVariable Long roverId,
            @PathVariable Long newPlanetId) {
        if (roverId == null || newPlanetId == null) {
            throw new IllegalArgumentException("RoverId and PlanetId must be provided");
        }
        RoverDTO updatedRover = service.updatePlanet(roverId, newPlanetId);
        linkUtil.createSelfLinkInRover(updatedRover);
        return new ResponseEntity<>(updatedRover, HttpStatus.OK);
    }

    @PutMapping("/{roverId}/plug")
    public ResponseEntity<RoverDTO> turnOnOff(@PathVariable Long roverId) {
        try {
            RoverDTO updatedRover = service.turnOnOff(roverId);
            linkUtil.createSelfLinkInRover(updatedRover);
            return new ResponseEntity<>(updatedRover, HttpStatus.OK);
        } catch (RoverNotFoundException e) {
            logger.log(Level.SEVERE, e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }

}
