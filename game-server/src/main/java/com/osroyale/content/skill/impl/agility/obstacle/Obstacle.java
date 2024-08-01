package com.osroyale.content.skill.impl.agility.obstacle;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.position.Position;

public final class Obstacle {
    private final ObstacleType type;
    private Position objectPosition;
    private final Position start;
    private final Position end;
    private final int level;
    private final float experience;
    private final int ordinal;
    private final Obstacle next;

    Obstacle(ObstacleBuilder builder) {
        type = builder.type;
        objectPosition = builder.objectPosition;
        start = builder.start;
        end = builder.end;
        level = builder.level;
        experience = builder.experience;
        ordinal = builder.ordinal;
        next = builder.next;
    }

    public void setObjectPosition(Position objectPosition) {
        this.objectPosition = objectPosition;
    }

    public Position getObjectPosition() {
        return objectPosition;
    }

    public Position getStart() {
        return start;
    }

    public Position getEnd() {
        return end;
    }

    public Obstacle getNext() {
        return next;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public ObstacleType getType() {
        return type;
    }

    public void execute(Player player) {
        type.execute(player, next, start, end, level, experience, ordinal);
    }

    @Override
    public String toString() {
        return "OBSTACLE [Type: " + type + ", Start: " + start + ", End: " + end + ", Level: " + level + ", Experience: " + experience + ", Ordinal: " + ordinal + "]";
    }

}