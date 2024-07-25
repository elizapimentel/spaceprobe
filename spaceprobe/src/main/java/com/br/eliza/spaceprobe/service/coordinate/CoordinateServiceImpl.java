package com.br.eliza.spaceprobe.service.coordinate;

import com.br.eliza.spaceprobe.enums.Direction;
import com.br.eliza.spaceprobe.model.Coordinates;
import com.br.eliza.spaceprobe.model.Planet;
import org.springframework.stereotype.Service;

@Service
public class CoordinateServiceImpl implements CoordinateService{

    @Override
    public boolean isOccupied(Coordinates coordinates, Planet planet) {
        return planet.getRovers().stream()
                .anyMatch(rover -> rover.getCoordinates().equals(coordinates));
    }

    @Override
    public Coordinates calculateNewCoordinates(Coordinates currentCoordinates,
                                               Direction direction) {

        int x = currentCoordinates.getX();
        int y = currentCoordinates.getY();

        switch (direction) {
            case NORTH:
                y++;
                break;
            case SOUTH:
                y--;
                break;
            case EAST:
                x++;
                break;
            case WEST:
                x--;
                break;
        }

        return new Coordinates(x, y);
    }

    @Override
    public Direction turnLeft(Direction direction) {
        return switch (direction) {
            case NORTH -> Direction.WEST;
            case EAST -> Direction.NORTH;
            case SOUTH -> Direction.EAST;
            case WEST -> Direction.SOUTH;
        };
    }

    @Override
    public Direction turnRight(Direction direction) {
        return switch (direction) {
            case NORTH -> Direction.EAST;
            case EAST -> Direction.SOUTH;
            case SOUTH -> Direction.WEST;
            case WEST -> Direction.NORTH;
        };
    }

}
