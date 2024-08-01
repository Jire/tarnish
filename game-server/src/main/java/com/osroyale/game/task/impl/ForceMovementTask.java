package com.osroyale.game.task.impl;

import com.osroyale.game.Animation;
import com.osroyale.game.task.Task;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.ForceMovement;
import com.osroyale.game.world.position.Position;

public class ForceMovementTask extends Task {
    private final Mob mob;
    private final Position start;
    private final Position end;
    private final Animation animation;
    private final ForceMovement forceMovement;

    private final int moveDelay;
    private int tick;

    public ForceMovementTask(Mob mob, int delay, ForceMovement forceMovement, Animation animation) {
        this(mob, delay, 0, forceMovement, animation);
    }

    public ForceMovementTask(Mob mob, int delay, int moveDelay, ForceMovement forceMovement, Animation animation) {
        super(delay == 0, delay);
        this.mob = mob;
        this.start = forceMovement.getStart().copy();
        this.end = forceMovement.getEnd().copy();
        this.animation = animation;
        this.forceMovement = forceMovement;
        this.moveDelay = moveDelay;
    }

    @Override
    public boolean canSchedule() {
        return mob.forceMovement == null;
    }

    @Override
    public void onSchedule() {
        mob.getCombat().reset();
        mob.movement.reset();
        mob.animate(animation, true);
        mob.setForceMovement(forceMovement);
    }

    @Override
    public void execute() {
        if (tick >= moveDelay) {
            final int x = start.getX() + end.getX();
            final int y = start.getY() + end.getY();
            mob.move(new Position(x, y, mob.getHeight()));
            mob.setForceMovement(null);
            cancel();
        }
        tick++;
    }
}
