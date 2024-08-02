package com.br.eliza.spaceprobe.controller.rover;

import com.br.eliza.spaceprobe.controller.planet.PlanetController;
import com.br.eliza.spaceprobe.dto.CommandDTO;
import com.br.eliza.spaceprobe.dto.RoverDTO;
import com.br.eliza.spaceprobe.exceptions.RoverNotFoundException;
import com.br.eliza.spaceprobe.service.rover.RoverService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/v2/rovers")
@Validated
public class RoverController {

    private final RoverService service;
    private final LinkConfig linkConfig;
    private static final Logger logger = Logger.getLogger(PlanetController.class.getName());


    public RoverController(RoverService service, LinkConfig linkConfig) {
        this.service = service;
        this.linkConfig = linkConfig;

    }

    @Operation(summary = "Add a new rover")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rover added successfully", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = RoverDTO.class),
                    examples = @ExampleObject(value = "{\n" +
                            "  \"roverId\": 1,\n" +
                            "  \"coordinates\": {\n" +
                            "    \"x\": 1,\n" +
                            "    \"y\": 2\n" +
                            "  },\n" +
                            "  \"direction\": \"NORTH\",\n" +
                            "  \"planetDTO\": null,\n" +
                            "  \"isOn\": true\n" +
                            "}"))),
    })
    @PostMapping("/add")
    public ResponseEntity<RoverDTO> save(@Valid @RequestBody RoverDTO rover) {
        RoverDTO savedRover = service.save(rover);
        linkConfig.createSelfLinkInRover(savedRover);
        return new ResponseEntity<>(savedRover, HttpStatus.CREATED);
    }

    @Operation(summary = "List all rovers")
    @ApiResponse(responseCode = "200", description = "A list of rovers", content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = RoverDTO.class),
            examples = @ExampleObject(value = "[{\n" +
                    "  \"roverId\": 1,\n" +
                    "  \"coordinates\": {\n" +
                    "    \"x\": 1,\n" +
                    "    \"y\": 2\n" +
                    "  },\n" +
                    "  \"direction\": \"NORTH\",\n" +
                    "  \"planetDTO\": null,\n" +
                    "  \"isOn\": true\n" +
                    "}]")))
    @GetMapping("/all")
    public ResponseEntity<List<RoverDTO>> findAll() {
        List<RoverDTO> rovers = service.findAll();
        rovers.forEach(linkConfig::createSelfLinkInCollectionsToRover);
        return new ResponseEntity<>(rovers, HttpStatus.OK);
    }

    @Operation(summary = "Move a rover based on commands")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rover moved successfully", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = RoverDTO.class),
                    examples = @ExampleObject(value = "{\n" +
                            "  \"roverId\": 1,\n" +
                            "  \"coordinates\": {\n" +
                            "    \"x\": 2,\n" +
                            "    \"y\": 2\n" +
                            "  },\n" +
                            "  \"direction\": \"EAST\",\n" +
                            "  \"planetDTO\": null,\n" +
                            "  \"isOn\": true\n" +
                            "}"))),
    })
    @PostMapping("/move")
    public ResponseEntity<RoverDTO> moveRover(@Valid @RequestBody CommandDTO commandDTO) {
        RoverDTO updatedRover = service.moveRover(commandDTO);
        linkConfig.createSelfLinkInRover(updatedRover);
        return new ResponseEntity<>(updatedRover, HttpStatus.OK);
    }

    @Operation(summary = "Find a rover by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rover found", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = RoverDTO.class),
                    examples = @ExampleObject(value = "{\n" +
                            "  \"roverId\": 1,\n" +
                            "  \"coordinates\": {\n" +
                            "    \"x\": 1,\n" +
                            "    \"y\": 2\n" +
                            "  },\n" +
                            "  \"direction\": \"NORTH\",\n" +
                            "  \"planetDTO\": null,\n" +
                            "  \"isOn\": true\n" +
                            "}"))),
    })
    @GetMapping("/{roverId}")
    public ResponseEntity<RoverDTO> findById(@PathVariable Long roverId) {
        try {
            RoverDTO rover = service.findById(roverId);
            linkConfig.createSelfLinkInRover(rover);
            return new ResponseEntity<>(rover, HttpStatus.OK);
        } catch (RoverNotFoundException e) {
            logger.log(Level.SEVERE, e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }

    @Operation(summary = "Update the planet assigned to a rover")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rover planet updated", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = RoverDTO.class),
                    examples = @ExampleObject(value = "{\n" +
                            "  \"roverId\": 1,\n" +
                            "  \"coordinates\": {\n" +
                            "    \"x\": 1,\n" +
                            "    \"y\": 2\n" +
                            "  },\n" +
                            "  \"direction\": \"NORTH\",\n" +
                            "  \"planetDTO\": {\n" +
                            "    \"planetId\": 2,\n" +
                            "    \"planetName\": \"Mars\",\n" +
                            "    \"width\": 5,\n" +
                            "    \"height\": 5,\n" +
                            "    \"rovers\": []\n" +
                            "  },\n" +
                            "  \"isOn\": true\n" +
                            "}"))),
    })
    @PutMapping("/{roverId}/planet/{newPlanetId}")
    public ResponseEntity<RoverDTO> updateRoverPlanet(
            @PathVariable Long roverId,
            @PathVariable Long newPlanetId) {
        if (roverId == null || newPlanetId == null) {
            throw new IllegalArgumentException("RoverId and PlanetId must be provided");
        }
        RoverDTO updatedRover = service.updatePlanet(roverId, newPlanetId);
        linkConfig.createSelfLinkInRover(updatedRover);
        return new ResponseEntity<>(updatedRover, HttpStatus.OK);
    }


    @Operation(summary = "Turn on or off a rover")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rover status updated", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = RoverDTO.class),
                    examples = @ExampleObject(value = "{\n" +
                            "  \"roverId\": 1,\n" +
                            "  \"coordinates\": {\n" +
                            "    \"x\": 1,\n" +
                            "    \"y\": 2\n" +
                            "  },\n" +
                            "  \"direction\": \"NORTH\",\n" +
                            "  \"planetDTO\": null,\n" +
                            "  \"isOn\": false\n" +
                            "}"))),
    })
    @PutMapping("/{roverId}/plug")
    public ResponseEntity<RoverDTO> turnOnOff(@PathVariable Long roverId) {
        try {
            RoverDTO updatedRover = service.turnOnOff(roverId);
            linkConfig.createSelfLinkInRover(updatedRover);
            return new ResponseEntity<>(updatedRover, HttpStatus.OK);
        } catch (RoverNotFoundException e) {
            logger.log(Level.SEVERE, e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }

}
