package com.br.eliza.spaceprobe.controller.planet;

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
    public ResponseEntity<Planet> createPlanet(@RequestBody Planet planet) {
        Planet savedPlanet = service.save(planet);
        return ResponseEntity.status(201).body(savedPlanet);
    }
}
