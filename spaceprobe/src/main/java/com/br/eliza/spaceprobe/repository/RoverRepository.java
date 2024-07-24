package com.br.eliza.spaceprobe.repository;

import com.br.eliza.spaceprobe.model.Rover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoverRepository extends JpaRepository<Rover, Long> {
}
