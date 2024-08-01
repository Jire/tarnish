package com.osroyale.content.skill.impl.agility.obstacle.impl;

import com.osroyale.content.skill.impl.agility.obstacle.ObstacleInteraction;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.position.Position;

public interface WalkInteraction extends ObstacleInteraction {
    @Override
    default void start(Player player) {
        player.mobAnimation.setWalk(getAnimation());
    }

    @Override
    default void onExecution(Player player, Position start, Position end) {
        player.movement.walk(end);
    }

    @Override
    default void onCancellation(Player player) { }
}