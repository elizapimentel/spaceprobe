package com.br.eliza.spaceprobe.util.config;

import com.br.eliza.spaceprobe.controller.planet.PlanetController;
import com.br.eliza.spaceprobe.controller.rover.RoverController;
import com.br.eliza.spaceprobe.dto.PlanetDTO;
import com.br.eliza.spaceprobe.dto.RoverDTO;
import com.br.eliza.spaceprobe.exceptions.PlanetNotFoundException;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class LinkConfig {

    public void createSelfLinkInPlanet(PlanetDTO planetDTO) {
        Link selfLink = linkTo(PlanetController.class).slash(planetDTO.getPlanetId())
                .withSelfRel();
        planetDTO.add(selfLink);
    }
    public void createSelfLinkInCollectionsToPlanet(PlanetDTO planetDTO)
            throws PlanetNotFoundException {
        Link selfLink = linkTo(methodOn(PlanetController.class).getPlanetById(planetDTO.getPlanetId()))
                .withSelfRel().expand();
        planetDTO.add(selfLink);
    }

    // Adiciona um link a um RoverDTO
    public void createSelfLinkInRover(RoverDTO roverDTO) {
        Link selfLink = linkTo(RoverController.class).slash(roverDTO.getRoverId())
                .withSelfRel();
        roverDTO.add(selfLink);
    }

    public void createSelfLinkInCollectionsToRover(RoverDTO roverDTO)
            throws PlanetNotFoundException {
        Link selfLink = linkTo(methodOn(RoverController.class).findById(roverDTO.getRoverId()))
                .withSelfRel().expand();
        roverDTO.add(selfLink);
    }

}
