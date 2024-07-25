package com.br.eliza.spaceprobe.controller.planet;

import com.br.eliza.spaceprobe.dto.PlanetDTO;
import com.br.eliza.spaceprobe.model.Coordinates;
import com.br.eliza.spaceprobe.service.planet.PlanetServiceImpl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/planets")
public class PlanetController {

    private final PlanetServiceImpl service;

    public PlanetController(PlanetServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/all")
    public ResponseEntity<List<PlanetDTO>> getAllPlanets() {
        List<PlanetDTO> planets = service.findAll();
        return ResponseEntity.status(200).body(planets);
    }

    @PostMapping("/add")
    public ResponseEntity<PlanetDTO> addPlanet(@RequestBody PlanetDTO planet) {
        PlanetDTO savedPlanet = service.save(planet);
        return ResponseEntity.status(201).body(savedPlanet);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanetDTO> getPlanetById(@PathVariable Long id) {
        PlanetDTO planet = service.findById(id);
        return ResponseEntity.status(200).body(planet);
    }

    @PostMapping("/{planetId}/{roverId}")
    public ResponseEntity<PlanetDTO> addRoverToPlanet(@PathVariable Long planetId, @PathVariable Long roverId) {
        try {
            PlanetDTO addedRover = service.addRover(planetId, roverId);
            return ResponseEntity.ok(addedRover);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @GetMapping("/{planetId}/isOccupied")
    public boolean isOccupied(@PathVariable Long planetId, @RequestBody Coordinates coordinates) {
        return service.isOccupied(planetId, coordinates);
    }


}
