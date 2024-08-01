package com.osroyale.content.skill.impl.agility.obstacle.impl;

import com.osroyale.content.skill.impl.agility.obstacle.ObstacleInteraction;
import com.osroyale.game.Animation;
import com.osroyale.game.task.Task;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.position.Position;

public interface SeersClimbInteraction extends ObstacleInteraction {

    @Override
    default void start(Player player) {
    }

    @Override
    default void onExecution(Player player, Position start, Position end) {
        player.animate(new Animation(getAnimation()));
        World.schedule(new Task(1) {
            int count = 0;

            @Override
            public void execute() {
                if (count == 0) {
                    player.move(new Position(player.getX(), player.getY(), player.getHeight() + 1));
                    player.animate(new Animation(1118));
                } else if (count == 2) {
                    player.move(end);
                    player.animate(new Animation(65535));
                    this.cancel();
                }
                count++;
            }
        });
    }

    @Override
    default void onCancellation(Player player) {
    }
}