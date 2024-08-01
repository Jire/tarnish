package com.osroyale.content.skill.impl.agility.obstacle.impl;

import com.osroyale.content.skill.impl.agility.obstacle.ObstacleInteraction;
import com.osroyale.game.task.Task;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.Direction;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.position.Position;

public interface SteppingStonesInteraction extends ObstacleInteraction {
    @Override
    default void start(Player player) {
    }

    @Override
    default void onExecution(Player player, Position start, Position end) {
        int dX = end.getX() - player.getPosition().getX();
        int dY = end.getY() - player.getPosition().getY();
        int modX = Integer.signum(dX);
        int modY = Integer.signum(dY);
        int totalSteps = Math.abs(modX) > Math.abs(modY) ? Math.abs(dX) : Math.abs(dY);

        World.schedule(new Task(true,3) {
            int steps = 0;

            @Override
            public void execute() {
                player.forceMove(1, getAnimation(), 10, 26, new Position(modX, modY), Direction.WEST);
                if (++steps == totalSteps) {
                    cancel();
                }
            }
        });
    }

    @Override
    default void onCancellation(Player player) {
    }
}