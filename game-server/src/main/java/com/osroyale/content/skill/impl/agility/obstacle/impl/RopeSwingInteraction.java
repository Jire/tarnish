package com.osroyale.content.skill.impl.agility.obstacle.impl;

import com.osroyale.content.skill.impl.agility.obstacle.ObstacleInteraction;
import com.osroyale.game.world.entity.mob.Direction;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.position.Position;
import com.osroyale.game.world.object.GameObject;
import com.osroyale.net.packet.out.SendObjectAnimation;

public interface RopeSwingInteraction extends ObstacleInteraction {
    @Override
    default void start(Player player) { }

    @Override
    default void onExecution(Player player, Position start, Position end) {
        int modX = end.getX() - player.getPosition().getX();
        int modY = end.getY() - player.getPosition().getY();

        Position destination = Position.create(modX, modY);
        Direction direction = Direction.getFollowDirection(player.getPosition(), end);
        GameObject object = player.attributes.get("AGILITY_OBJ", GameObject.class);

        player.send(new SendObjectAnimation(497, object));
        player.forceMove(3, 751, 33, 60, destination, direction);
    }

    @Override
    default void onCancellation(Player player) { }
}