package com.osroyale.content.skill.impl.agility.obstacle;

import com.osroyale.game.world.position.Position;

public class ObstacleBuilder {
    public final ObstacleType type;
    public final Position objectPosition;
    public final Position start;
    public final Position end;
    public int level;
    public float experience;
    public int ordinal;
    public Obstacle next;

    public ObstacleBuilder(ObstacleType type, Position objectPosition, Position start, Position end) {
        this.objectPosition = objectPosition;
        this.type = type;
        this.end = end;
        this.start = start;
        level = 1;
        experience = 0;
        ordinal = -1;
    }

    public ObstacleBuilder setLevel(int level) {
        this.level = level;
        return this;
    }

    public ObstacleBuilder setExperience(float experience) {
        this.experience = experience;
        return this;
    }

    public ObstacleBuilder setOrdinal(int ordinal) {
        this.ordinal = ordinal;
        return this;
    }

    public ObstacleBuilder setNext(Obstacle next) {
        this.next = next;
        return this;
    }

    public Obstacle build() {
        return new Obstacle(this);
    }
}