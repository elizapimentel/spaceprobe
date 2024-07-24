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
    private Coordenates coordenates;

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
                if (coordenates.getY() + 1 < planet.getHeight()) {
                    coordenates.setY(coordenates.getY() + 1);
                }
                break;
            case SOUTH:
                if (coordenates.getY() - 1 >= 0) {
                    coordenates.setY(coordenates.getY() - 1);
                }
                break;
            case EAST:
                if (coordenates.getX() + 1 < planet.getWidth()) {
                    coordenates.setX(coordenates.getX() + 1);
                }
                break;
            case WEST:
                if (coordenates.getX() - 1 >= 0) {
                    coordenates.setX(coordenates.getX() - 1);
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
