package com.br.eliza.spaceprobe.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Planet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planetId;
    private String planetName;
    private int width;
    private int height;

    @OneToMany(mappedBy = "planets", cascade = CascadeType.ALL)
    private List<Rover> rovers = new ArrayList<>();

    public boolean addRover(Rover rover) {
        if (rovers.size() < 5) {
            rovers.add(rover);
            rover.setPlanet(this);
            return true;
        } else {
            throw new RuntimeException("Planet is full");
        }
    }

}
