package com.osroyale.content.skill.impl.agility.obstacle.impl;

import com.osroyale.content.skill.impl.agility.obstacle.ObstacleInteraction;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.UpdateFlag;
import com.osroyale.game.world.position.Position;

public interface RunningStartInteraction extends ObstacleInteraction {
    @Override
    default void start(Player player) {
        player.mobAnimation.setWalk(getAnimation());
    }

    @Override
    default void onExecution(Player player, Position start, Position end) {
        player.getCombat().reset();
        player.movement.walk(end);
        player.updateFlags.add(UpdateFlag.APPEARANCE);
    }

    @Override
    default void onCancellation(Player player) { }
}