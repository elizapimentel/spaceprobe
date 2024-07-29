package com.br.eliza.spaceprobe.controller.planet;

import com.br.eliza.spaceprobe.dto.PlanetDTO;
import com.br.eliza.spaceprobe.dto.RoverDTO;
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

    @PostMapping("/{planetId}")
    public ResponseEntity<PlanetDTO> addRoverToPlanet(@PathVariable Long planetId, @RequestBody RoverDTO rover) {
        PlanetDTO addedRover = service.addRover(planetId, rover);
        return ResponseEntity.status(200).body(addedRover);
    }

    @GetMapping("/{planetId}/isOccupied")
    public boolean isOccupied(@PathVariable Long planetId,
                              @Valid @RequestBody Coordinates coordinates) {
        return service.isOccupied(planetId, coordinates);

    }
}
