package com.br.eliza.spaceprobe.controller.planet;

import com.br.eliza.spaceprobe.dto.PlanetDTO;
import com.br.eliza.spaceprobe.dto.RoverDTO;
import com.br.eliza.spaceprobe.exceptions.PlanetNotFoundException;
import com.br.eliza.spaceprobe.model.Coordinates;
import com.br.eliza.spaceprobe.service.planet.PlanetService;
import com.br.eliza.spaceprobe.service.planet.PlanetServiceImpl;

import com.br.eliza.spaceprobe.util.LinkUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


@RestController
@RequestMapping("/v2/planets")
public class PlanetController {

    private final PlanetService service;
    private final LinkUtil linkUtil;
    private static final Logger logger = Logger.getLogger(PlanetController.class.getName());

    public PlanetController(PlanetService service, LinkUtil linkUtil) {
        this.service = service;
        this.linkUtil = linkUtil;
    }

    @GetMapping("/all")
    public ResponseEntity<List<PlanetDTO>> getAllPlanets() {
        List<PlanetDTO> planets = service.findAll();
        planets.stream().forEach(linkUtil::createSelfLinkInCollectionsToPlanet);
        return new ResponseEntity<>(planets, HttpStatus.OK);

    }

    @PostMapping("/add")
    public ResponseEntity<PlanetDTO> addPlanet(@Valid @RequestBody PlanetDTO planet) {
        PlanetDTO savedPlanet = service.save(planet);
        linkUtil.createSelfLinkInPlanet(savedPlanet);
        return new ResponseEntity<>(savedPlanet, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanetDTO> getPlanetById(@PathVariable Long id) {
        try {
            PlanetDTO planet = service.findById(id);
            linkUtil.createSelfLinkInPlanet(planet);
            return new ResponseEntity<>(planet, HttpStatus.OK);
        } catch (PlanetNotFoundException e) {
            logger.log(Level.SEVERE, e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{planetId}")
    public ResponseEntity<PlanetDTO> addRoverToPlanet(@PathVariable Long planetId, @RequestBody RoverDTO rover) {
        PlanetDTO addedRover = service.addRover(planetId, rover);
        linkUtil.createSelfLinkInPlanet(addedRover);
        return new ResponseEntity<>(addedRover, HttpStatus.OK);
    }

    @GetMapping("/{planetId}/isOccupied")
    public boolean isOccupied(@PathVariable Long planetId,
                              @Valid @RequestBody Coordinates coordinates) {
        return service.isOccupied(planetId, coordinates);

    }


}
