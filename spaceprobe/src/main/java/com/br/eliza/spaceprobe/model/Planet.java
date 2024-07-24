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
public class PlanetModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planetId;
    private String planetName;
    private int width;
    private int height;

    @OneToMany(mappedBy = "planets", cascade = CascadeType.ALL)
    private List<ProbeModel> probes = new ArrayList<>();

    public boolean addProbe(ProbeModel probe) {
        if (probe.() < 5) {
            probe.add(probe);
            probe.setPlanet(this);
            return true;
        }
        return false;
    }

}
