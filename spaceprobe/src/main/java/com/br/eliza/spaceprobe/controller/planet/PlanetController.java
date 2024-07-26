package com.br.eliza.spaceprobe.controller.planet;

import com.br.eliza.spaceprobe.dto.PlanetDTO;
import com.br.eliza.spaceprobe.model.Coordinates;
import com.br.eliza.spaceprobe.service.planet.PlanetServiceImpl;

import jakarta.validation.Valid;
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
    public ResponseEntity<PlanetDTO> addPlanet(@Valid @RequestBody PlanetDTO planet) {
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
        PlanetDTO addedRover = service.addRover(planetId, roverId);
        return ResponseEntity.status(200).body(addedRover);
    }

    @GetMapping("/{planetId}/isOccupied")
    public ResponseEntity<Boolean> isOccupied(
            @PathVariable Long planetId,
            @RequestParam int x,
            @RequestParam int y) {
        boolean occupied =  service.isOccupied(planetId, new Coordinates(x, y));
        return ResponseEntity.status(200).body(occupied);
    }


}
