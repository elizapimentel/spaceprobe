package com.br.eliza.spaceprobe.model;

import com.br.eliza.spaceprobe.dto.PlanetDTO;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Planet implements Serializable {

    @Serial
    private static final long serialVersionUID = -8457080915399394686L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    @Id
    private Long planetId;

    @NotNull
    private String planetName;
    private int width;
    private int height;

    @OneToMany(mappedBy = "planet", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Rover> rovers = new ArrayList<>();

    public PlanetDTO convertEntityToDto() {
        return new ModelMapper().map(this, PlanetDTO.class);
    }

}
