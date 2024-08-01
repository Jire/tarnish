package com.osroyale.content.skill.impl.agility.obstacle.impl;

import com.osroyale.content.skill.impl.agility.obstacle.ObstacleInteraction;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.Direction;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.position.Position;

public interface SeersJumpGapInteraction2 extends ObstacleInteraction {

    @Override
    default void onExecution(Player player, Position start, Position end) {
        final Position edge = new Position(start.getX(), start.getY() - 3, 3);
        final Position finish = new Position(start.getX(), start.getY() - 4, 3);

        player.face(Direction.SOUTH);
        player.animate(2586, true);
        World.schedule(() -> {
            player.move(edge);
            player.face(Direction.SOUTH);
            player.animate(2585, true);
        });
        World.schedule(3, () -> {
            player.move(finish);
            player.face(Direction.SOUTH);
        });
    }

}
