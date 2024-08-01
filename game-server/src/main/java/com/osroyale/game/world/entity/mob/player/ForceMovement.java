package com.osroyale.game.world.entity.mob.player;

import com.osroyale.game.world.entity.mob.Direction;
import com.osroyale.game.world.position.Position;

public class ForceMovement {
    private final Position start;
    private final Position end;
    private final int speed;
    private final int reverseSpeed;
    private final int direction;

    public ForceMovement(Position start, Position end, int speed, int reverseSpeed, Direction direction) {
        this.start = start;
        this.end = end;
        this.speed = speed;
        this.reverseSpeed = reverseSpeed;
        switch (direction) {
            case NORTH:
                this.direction = 0;
                break;
            case EAST:
                this.direction = 1;
                break;
            case SOUTH:
                this.direction = 2;
                break;
            case WEST:
                this.direction = 3;
                break;
            default:
                throw new IllegalArgumentException(String.format("Invalid force movement direction=%s", direction.name()));
        }
    }

    public Position getStart() {
        return start;
    }

    public Position getEnd() {
        return end;
    }

    public int getSpeed() {
        return speed;
    }

    public int getReverseSpeed() {
        return reverseSpeed;
    }

    public int getDirection() {
        return direction;
    }

}