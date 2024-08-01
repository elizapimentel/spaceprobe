package com.br.eliza.spaceprobe.service.coordinate;


import com.br.eliza.spaceprobe.enums.Direction;
import com.br.eliza.spaceprobe.exceptions.InvalidCommandException;
import com.br.eliza.spaceprobe.model.Coordinates;
import com.br.eliza.spaceprobe.model.Planet;
import com.br.eliza.spaceprobe.model.Rover;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CoordinateServiceImplTest {

    private CoordinateServiceImpl coordinateService;

    @BeforeEach
    public void setUp() {
        coordinateService = new CoordinateServiceImpl();
    }

    @Test
    public void testIsOccupiedTrue() {
        Coordinates coordinates = new Coordinates(1, 1);
        Rover rover = new Rover();
        rover.setCoordinates(coordinates);
        Planet planet = mock(Planet.class);
        when(planet.getRovers()).thenReturn(List.of(rover));

        assertTrue(coordinateService.isOccupied(coordinates, planet));
    }

    @Test
    public void testIsOccupiedFalse() {
        Coordinates coordinates = new Coordinates(1, 1);
        Rover rover = new Rover();
        rover.setCoordinates(new Coordinates(2, 2));
        Planet planet = mock(Planet.class);
        when(planet.getRovers()).thenReturn(List.of(rover));

        assertFalse(coordinateService.isOccupied(coordinates, planet));
    }

    @Test
    public void testCalculateNewCoordinatesNorth() {
        Coordinates currentCoordinates = new Coordinates(1, 1);
        Coordinates newCoordinates = coordinateService.calculateNewCoordinates(currentCoordinates, Direction.NORTH);
        assertEquals(new Coordinates(1, 2), newCoordinates);
    }

    @Test
    public void testCalculateNewCoordinatesSouth() {
        Coordinates currentCoordinates = new Coordinates(1, 1);
        Coordinates newCoordinates = coordinateService.calculateNewCoordinates(currentCoordinates, Direction.SOUTH);
        assertEquals(new Coordinates(1, 0), newCoordinates);
    }

    @Test
    public void testCalculateNewCoordinatesEast() {
        Coordinates currentCoordinates = new Coordinates(1, 1);
        Coordinates newCoordinates = coordinateService.calculateNewCoordinates(currentCoordinates, Direction.EAST);
        assertEquals(new Coordinates(2, 1), newCoordinates);
    }

    @Test
    public void testCalculateNewCoordinatesWest() {
        Coordinates currentCoordinates = new Coordinates(1, 1);
        Coordinates newCoordinates = coordinateService.calculateNewCoordinates(currentCoordinates, Direction.WEST);
        assertEquals(new Coordinates(0, 1), newCoordinates);
    }

    @Test
    public void testCalculateNewCoordinatesNullCoordinates() {
        assertThrows(InvalidCommandException.class, () ->
                coordinateService.calculateNewCoordinates(null, Direction.NORTH));
    }

    @Test
    public void testCalculateNewCoordinatesNullDirection() {
        Coordinates currentCoordinates = new Coordinates(1, 1);
        assertThrows(InvalidCommandException.class, () ->
                coordinateService.calculateNewCoordinates(currentCoordinates, null));
    }

    @Test
    public void testCalculateNewCoordinatesNegativeCoordinates() {
        Coordinates currentCoordinates = new Coordinates(-1, -1);
        assertThrows(InvalidCommandException.class, () ->
                coordinateService.calculateNewCoordinates(currentCoordinates, Direction.NORTH));
    }

    @Test
    public void testTurnLeft() {
        assertEquals(Direction.WEST, coordinateService.turnLeft(Direction.NORTH));
        assertEquals(Direction.SOUTH, coordinateService.turnLeft(Direction.WEST));
        assertEquals(Direction.EAST, coordinateService.turnLeft(Direction.SOUTH));
        assertEquals(Direction.NORTH, coordinateService.turnLeft(Direction.EAST));
    }

    @Test
    public void testTurnRight() {
        assertEquals(Direction.EAST, coordinateService.turnRight(Direction.NORTH));
        assertEquals(Direction.SOUTH, coordinateService.turnRight(Direction.EAST));
        assertEquals(Direction.WEST, coordinateService.turnRight(Direction.SOUTH));
        assertEquals(Direction.NORTH, coordinateService.turnRight(Direction.WEST));
    }

}
