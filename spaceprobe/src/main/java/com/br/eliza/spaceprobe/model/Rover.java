package com.br.eliza.spaceprobe.model;

import com.br.eliza.spaceprobe.dto.PlanetDTO;
import com.br.eliza.spaceprobe.dto.RoverDTO;
import com.br.eliza.spaceprobe.enums.Direction;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rover implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roverId;

    @Embedded
    @NotNull
    private Coordinates coordinates;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Direction direction;

    @ManyToOne
    @JoinColumn(name = "planet_id")
    @JsonBackReference
    private Planet planet;

    private Boolean isOn;

    public RoverDTO convertEntityToDto() {
        ModelMapper modelMapper = new ModelMapper();
        RoverDTO roverDTO = modelMapper.map(this, RoverDTO.class);
        if (this.planet != null) {
            roverDTO.setPlanetDTO(modelMapper.map(this.planet, PlanetDTO.class));
        }
        return roverDTO;
    }

}
