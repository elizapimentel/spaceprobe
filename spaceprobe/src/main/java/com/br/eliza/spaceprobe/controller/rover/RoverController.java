package com.br.eliza.spaceprobe.controller.rover;

import com.br.eliza.spaceprobe.dto.RoverCommandDTO;
import com.br.eliza.spaceprobe.model.Rover;
import com.br.eliza.spaceprobe.service.rover.RoverService;
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
    public ResponseEntity<Rover> save(@RequestBody Rover rover) {
        return ResponseEntity.ok(service.save(rover));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Rover>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping("/move")
    public ResponseEntity<Rover> moveRover(@RequestBody RoverCommandDTO commandDTO) {
        Rover updatedRover  = service.moveRover(commandDTO);
        return ResponseEntity.ok(updatedRover);
    }

    @GetMapping("/{roverId}")
    public ResponseEntity<Rover> findById(@PathVariable Long roverId) {
        Rover rover = service.findById(roverId);
        return ResponseEntity.ok(rover);
    }

    @PutMapping("/{roverId}/planet/{newPlanetId}")
    public ResponseEntity<Rover> updateRoverPlanet(
            @PathVariable Long roverId,
            @PathVariable Long newPlanetId) {

        try {
            Rover updatedRover = service.updatePlanet(roverId, newPlanetId);
            return new ResponseEntity<>(updatedRover, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{roverId}/plug")
    public ResponseEntity<Rover> turnOnOff(@PathVariable Long roverId) {
        Rover updatedRover = service.turnOnOff(roverId);
        return ResponseEntity.ok(updatedRover);
    }

}
