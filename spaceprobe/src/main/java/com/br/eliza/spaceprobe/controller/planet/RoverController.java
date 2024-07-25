package com.br.eliza.spaceprobe.controller.planet;

import com.br.eliza.spaceprobe.dto.RoverCommandDTO;
import com.br.eliza.spaceprobe.model.Rover;
import com.br.eliza.spaceprobe.service.rover.RoverService;
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
}
