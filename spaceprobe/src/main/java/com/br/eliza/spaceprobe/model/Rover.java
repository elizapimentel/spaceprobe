package com.br.eliza.spaceprobe.model;

import com.br.eliza.spaceprobe.enums.Direction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rover {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roverId;

    @Embedded
    private Coordinates coordinates;

    @Enumerated(EnumType.STRING)
    private Direction direction;

    @ManyToOne
    @JoinColumn(name = "planet_id")
    private Planet planet;

    public void processCommands(List<Character> commands) {
        for (char command : commands) {
            switch (command) {
                case 'M':
                    move();
                    break;
                case 'L':
                    turnLeft();
                    break;
                case 'R':
                    turnRight();
                    break;
                default:
                    throw new IllegalArgumentException("Invalid command: " + command);
            }
        }
    }

    private void move() {
        switch (direction) {
            case NORTH:
                if (coordinates.getY() + 1 < planet.getHeight()) {
                    coordinates.setY(coordinates.getY() + 1);
                }
                break;
            case SOUTH:
                if (coordinates.getY() - 1 >= 0) {
                    coordinates.setY(coordinates.getY() - 1);
                }
                break;
            case EAST:
                if (coordinates.getX() + 1 < planet.getWidth()) {
                    coordinates.setX(coordinates.getX() + 1);
                }
                break;
            case WEST:
                if (coordinates.getX() - 1 >= 0) {
                    coordinates.setX(coordinates.getX() - 1);
                }
                break;
        }
    }

    private void turnLeft() {
        direction = direction.turnLeft();
    }

    private void turnRight() {
        direction = direction.turnRight();
    }
}
