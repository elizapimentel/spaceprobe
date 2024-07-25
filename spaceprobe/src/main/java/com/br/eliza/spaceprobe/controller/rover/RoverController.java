package com.br.eliza.spaceprobe.controller.rover;

import com.br.eliza.spaceprobe.dto.CommandDTO;
import com.br.eliza.spaceprobe.dto.RoverDTO;
import com.br.eliza.spaceprobe.service.rover.RoverService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/rovers")
public class RoverController {

    private final RoverService service;

    public RoverController(RoverService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public ResponseEntity<RoverDTO> save(@Valid @RequestBody RoverDTO rover) {
        return ResponseEntity.ok(service.save(rover));
    }

    @GetMapping("/all")
    public ResponseEntity<List<RoverDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping("/move")
    public ResponseEntity<RoverDTO> moveRover(@Valid @RequestBody CommandDTO commandDTO) {
        RoverDTO updatedRover  = service.moveRover(commandDTO);
        return ResponseEntity.ok(updatedRover);
    }

    @GetMapping("/{roverId}")
    public ResponseEntity<RoverDTO> findById(@PathVariable Long roverId) {
        RoverDTO rover = service.findById(roverId);
        return ResponseEntity.ok(rover);
    }

    @PutMapping("/{roverId}/planet/{newPlanetId}")
    public ResponseEntity<RoverDTO> updateRoverPlanet(
            @PathVariable Long roverId,
            @PathVariable Long newPlanetId) {

        try {
            RoverDTO updatedRover = service.updatePlanet(roverId, newPlanetId);
            return new ResponseEntity<>(updatedRover, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{roverId}/plug")
    public ResponseEntity<RoverDTO> turnOnOff(@PathVariable Long roverId) {
        RoverDTO updatedRover = service.turnOnOff(roverId);
        return ResponseEntity.ok(updatedRover);
    }

}
