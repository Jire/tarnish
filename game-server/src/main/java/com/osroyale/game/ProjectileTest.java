package com.osroyale.game;

import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.position.Position;

public class ProjectileTest {

    Position start, target, offset;
    private final int creatorSize, startDistanceOffset, lockon, delay, startHeight, endHeight, projectileId, speed, slope, angle, radius, stepMultiplier;

    public ProjectileTest(Position start, Position end, int lockon,
                      int projectileId, int speed, int delay, int startHeight, int endHeight,
                      int curve, int creatorSize, int startDistanceOffset, int stepMultiplier) {
        this.start = start;
        this.target = end;
        this.offset = new Position((end.getX() - start.getX()),
                (end.getY() - start.getY()));
        this.creatorSize = creatorSize;
        this.startDistanceOffset = startDistanceOffset;
        this.lockon = lockon;
        this.projectileId = projectileId;
        this.delay = delay;
        this.speed = speed;
        this.startHeight = startHeight;
        this.endHeight = endHeight;
        this.slope = curve;
        this.angle = getAngle();
        this.radius = getRadius();
        this.stepMultiplier = stepMultiplier;
    }

    public ProjectileTest(Mob source, Mob victim, int projectileId,
                      int delay, int speed, int startHeight, int endHeight, int curve, int creatorSize, int stepMultiplier) {
        this(source.getPosition(), victim.getPosition(),
                (victim.isPlayer() ? -victim.getIndex() - 1
                        : victim.getIndex() + 1), projectileId, speed, delay,
                startHeight, endHeight, curve, creatorSize, 64, stepMultiplier);
    }

    public ProjectileTest(Position start, Position end, int lockon,
                      int projectileId, int speed, int delay, int startHeight, int endHeight,
                      int curve) {
        this(start, end, lockon, projectileId, speed, delay, startHeight, endHeight, curve, 1, 0, 0);
    }

    public ProjectileTest(Position source, Position delta, int slope, int speed, int projectileId, int startHeight, int endHeight, int lockon, int delay, int creatorSize, int startDistanceOffset) {
        this(source, delta, lockon, projectileId, speed, delay, startHeight, endHeight, slope, creatorSize, startDistanceOffset, 0);
    }

    public int getLockon() {
        return this.lockon;
    }

    public int getProjectileId() {
        return this.projectileId;
    }

    public int getStepMultiplier() {
        return stepMultiplier;
    }

    public int getSlope() {
        return this.slope;
    }

    public int getEndHeight() {
        return this.endHeight;
    }

    public int getStartHeight() {
        return this.startHeight;
    }

    public int getSpeed() {
        return this.speed;
    }

    public int getDelay() {
        return this.delay;
    }

    public int getCreatorSize() {
        return this.creatorSize;
    }

    public Position getOffset() {
        return this.offset;
    }

    public int getStartDistanceOffset() {
        return this.startDistanceOffset;
    }

    public Position getStart() {
        return this.start;
    }

    public Position getTarget() {
        return this.target;
    }

    public int getAngle() {
        return this.angle;
    }

    public int getRadius() {
        return this.radius;
    }

    public int getDuration(int distance) {
        if (distance > 0) {
            return this.delay + (distance * this.stepMultiplier);
        }
        return 0;
    }

    public int getHitDelay(int distance) {
        return (int) Math.floor(getDuration(distance) / 30D) + 1; //might be - 1
    }
}

