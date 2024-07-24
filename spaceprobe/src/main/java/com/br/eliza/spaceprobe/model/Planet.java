package com.br.eliza.spaceprobe.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Planet implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    @Id
    private Long planetId;

    private String planetName;
    private int width;
    private int height;

    @OneToMany(mappedBy = "planet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rover> rovers = new ArrayList<>();

    public void addRover(Rover rover) {
        rovers.add(rover);
        rover.setPlanet(this);
    }

    public void removeRover(Rover rover) {
        rovers.remove(rover);
        rover.setPlanet(null);
    }

}
