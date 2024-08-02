package com.br.eliza.spaceprobe.controller.planet;

import com.br.eliza.spaceprobe.dto.PlanetDTO;
import com.br.eliza.spaceprobe.dto.RoverDTO;
import com.br.eliza.spaceprobe.exceptions.PlanetNotFoundException;
import com.br.eliza.spaceprobe.model.Coordinates;
import com.br.eliza.spaceprobe.service.planet.PlanetService;

import com.br.eliza.spaceprobe.util.config.LinkConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


@RestController
@RequestMapping("/v2/planets")
public class PlanetController {

    private final PlanetService service;
    private final LinkConfig linkConfig;
    private static final Logger logger = Logger.getLogger(PlanetController.class.getName());

    public PlanetController(PlanetService service, LinkConfig linkConfig) {
        this.service = service;
        this.linkConfig = linkConfig;
    }

    @Operation(summary = "List all available planets")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A list of planets", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PlanetDTO.class),
                    examples = @ExampleObject(value = "[{\n" +
                            "  \"id\": \"1\",\n" +
                            "  \"name\": \"Mars\",\n" +
                            "  \"width\": \"5\",\n" +
                            "  \"height\": \"5\",\n" +
                            "  \"rovers\": [\n" +
                            "    {\n" +
                            "      \"roverId\": 1,\n" +
                            "      \"coordinates\": {\n" +
                            "        \"x\": 1,\n" +
                            "        \"y\": 2\n" +
                            "      },\n" +
                            "      \"direction\": \"NORTH\",\n" +
                            "      \"planetDTO\": null,\n" +
                            "      \"isOn\": true\n" +
                            "    }\n" +
                            "  ]\n" +
                            "}]")))
    })
    @GetMapping("/all")
    public ResponseEntity<List<PlanetDTO>> getAllPlanets() {
        List<PlanetDTO> planets = service.findAll();
        planets.stream().forEach(linkConfig::createSelfLinkInCollectionsToPlanet);
        return new ResponseEntity<>(planets, HttpStatus.OK);

    }


    @Operation(summary = "Add a planet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Planet added successfully", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PlanetDTO.class),
                    examples = @ExampleObject(value = "{\n" +
                            "  \"planetId\": 1,\n" +
                            "  \"planetName\": \"Mars\",\n" +
                            "  \"width\": 5,\n" +
                            "  \"height\": 5,\n" +
                            "  \"rovers\": []\n" +
                            "}")))
})
    @PostMapping("/add")
    public ResponseEntity<PlanetDTO> addPlanet(@Valid @RequestBody PlanetDTO planet) {
        PlanetDTO savedPlanet = service.save(planet);
        linkConfig.createSelfLinkInPlanet(savedPlanet);
        return new ResponseEntity<>(savedPlanet, HttpStatus.CREATED);
    }

    @Operation(summary = "Find a planet by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Planet found", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PlanetDTO.class),
                    examples = @ExampleObject(value = "{\n" +
                            "  \"planetId\": 1,\n" +
                            "  \"planetName\": \"Mars\",\n" +
                            "  \"width\": 5,\n" +
                            "  \"height\": 5,\n" +
                            "  \"rovers\": []\n" +
                            "}")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<PlanetDTO> getPlanetById(@PathVariable Long id) {
        try {
            PlanetDTO planet = service.findById(id);
            linkConfig.createSelfLinkInPlanet(planet);
            return new ResponseEntity<>(planet, HttpStatus.OK);
        } catch (PlanetNotFoundException e) {
            logger.log(Level.SEVERE, e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Add a rover to a planet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rover added to planet", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PlanetDTO.class),
                    examples = @ExampleObject(value = "{\n" +
                            "  \"planetId\": 1,\n" +
                            "  \"planetName\": \"Mars\",\n" +
                            "  \"width\": 5,\n" +
                            "  \"height\": 5,\n" +
                            "  \"rovers\": [\n" +
                            "    {\n" +
                            "      \"roverId\": 1,\n" +
                            "      \"coordinates\": {\n" +
                            "        \"x\": 1,\n" +
                            "        \"y\": 2\n" +
                            "      },\n" +
                            "      \"direction\": \"NORTH\",\n" +
                            "      \"planetDTO\": null,\n" +
                            "      \"isOn\": true\n" +
                            "    }\n" +
                            "  ]\n" +
                            "}")))
    })
    @PostMapping("/{planetId}")
    public ResponseEntity<PlanetDTO> addRoverToPlanet(@PathVariable Long planetId, @RequestBody RoverDTO rover) {
        PlanetDTO addedRover = service.addRover(planetId, rover);
        linkConfig.createSelfLinkInPlanet(addedRover);
        return new ResponseEntity<>(addedRover, HttpStatus.OK);
    }

    @Operation(summary = "Check if a planet is occupied by coordinates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Occupation status", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(type = "boolean")))
    })
    @GetMapping("/{planetId}/isOccupied")
    public boolean isOccupied(@PathVariable Long planetId,
                              @Valid @RequestBody Coordinates coordinates) {
        return service.isOccupied(planetId, coordinates);

    }


}
