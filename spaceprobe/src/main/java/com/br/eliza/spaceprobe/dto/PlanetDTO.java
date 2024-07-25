package com.br.eliza.spaceprobe.dto;

import com.br.eliza.spaceprobe.model.Planet;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PlanetDTO extends RepresentationModel<PlanetDTO> {

    private Long planetId;
    @NotNull(message="Name must be informed")
    private String planetName;
    private int width;
    private int height;
    private List<RoverDTO> rovers;

    public Planet convertDtoToEntity() {
        return new ModelMapper().map(this, Planet.class);
    }

}
