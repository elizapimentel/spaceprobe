package com.br.eliza.spaceprobe.controller.planet;

import com.br.eliza.spaceprobe.model.Coordinates;
import com.br.eliza.spaceprobe.model.Planet;
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
    public ResponseEntity<List<Planet>> getAllPlanets() {
        List<Planet> planets = service.findAll();
        return ResponseEntity.status(200).body(planets);
    }

    @PostMapping("/add")
    public ResponseEntity<Planet> addPlanet(@RequestBody Planet planet) {
        Planet savedPlanet = service.save(planet);
        return ResponseEntity.status(201).body(savedPlanet);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Planet> getPlanetById(@PathVariable Long id) {
        Planet planet = service.findById(id);
        return ResponseEntity.status(200).body(planet);
    }

    @PostMapping("/{planetId}/{roverId}")
    public ResponseEntity<Planet> addRoverToPlanet(@PathVariable Long planetId, @PathVariable Long roverId) {
        try {
            Planet addedRover = service.addRover(planetId, roverId);
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
