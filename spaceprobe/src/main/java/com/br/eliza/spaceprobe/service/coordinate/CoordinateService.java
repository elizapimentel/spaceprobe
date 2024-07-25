package com.br.eliza.spaceprobe.service.coordinate;

import com.br.eliza.spaceprobe.enums.Direction;
import com.br.eliza.spaceprobe.model.Coordinates;
import com.br.eliza.spaceprobe.model.Planet;

public interface CoordinateService {
    boolean isOccupied(Coordinates coordinates, Planet planet);
    Coordinates calculateNewCoordinates(Coordinates currentCoordinates, Direction currentDirection);
    Direction turnLeft(Direction currentDirection);
    Direction turnRight(Direction currentDirection);
}
