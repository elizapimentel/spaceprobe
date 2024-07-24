package com.br.eliza.spaceprobe.repository;

import com.br.eliza.spaceprobe.model.Planet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanetRepository extends JpaRepository<Planet, Long> {
}
