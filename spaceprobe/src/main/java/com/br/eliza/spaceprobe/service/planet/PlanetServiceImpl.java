package com.br.eliza.spaceprobe.service.planet;

import com.br.eliza.spaceprobe.model.Planet;
import com.br.eliza.spaceprobe.repository.PlanetRepository;
import com.br.eliza.spaceprobe.repository.RoverRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanetServiceImpl implements PlanetService{

    private final PlanetRepository planetRepo;
    private final RoverRepository roverRepo;

    public PlanetServiceImpl(PlanetRepository planetRepo, RoverRepository roverRepo) {
        this.planetRepo = planetRepo;
        this.roverRepo = roverRepo;
    }

    @Override
    @Transactional
    public Planet save(Planet planet) {
        return planetRepo.save(planet);
    }

    @Override
    public List<Planet> findAll() {
        return planetRepo.findAll();
    }


}