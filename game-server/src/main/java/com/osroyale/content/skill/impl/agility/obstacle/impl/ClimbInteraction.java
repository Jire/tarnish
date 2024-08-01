package com.osroyale.content.skill.impl.agility.obstacle.impl;

import com.osroyale.content.skill.impl.agility.obstacle.ObstacleInteraction;
import com.osroyale.game.Animation;
import com.osroyale.game.task.Task;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.position.Position;

public interface ClimbInteraction extends ObstacleInteraction {
    @Override
    default void start(Player player) { }

    @Override
    default void onExecution(Player player, Position start, Position end) {
        player.animate(new Animation(getAnimation()));
        World.schedule(new Task(2) {
            @Override
            public void execute() {
                player.move(end);
                player.animate(new Animation(65535));
                this.cancel();
            }
        });
    }

    @Override
    default void onCancellation(Player player) { }
}