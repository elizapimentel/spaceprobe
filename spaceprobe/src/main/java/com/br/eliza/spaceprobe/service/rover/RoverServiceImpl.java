package com.br.eliza.spaceprobe.service.rover;

import com.br.eliza.spaceprobe.model.Rover;
import com.br.eliza.spaceprobe.repository.RoverRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoverServiceImpl implements RoverService {

    private final RoverRepository roverRepo;

    public RoverServiceImpl(RoverRepository roverRepo) {
        this.roverRepo = roverRepo;
    }

    @Transactional
    @Override
    public Rover save(Rover rover) {
        return roverRepo.save(rover);
    }

    @Override
    public List<Rover> findAll() {
        return roverRepo.findAll();
    }

}
