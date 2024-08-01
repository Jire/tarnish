package com.osroyale.content.skill.impl.agility.obstacle.impl;

import com.osroyale.content.skill.impl.agility.obstacle.ObstacleInteraction;
import com.osroyale.game.Animation;
import com.osroyale.game.task.Task;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.position.Position;

public interface SeersJumpGapInteraction extends ObstacleInteraction {
    @Override
    default void start(Player player) {
//        player.getUpdateFlags().sendFaceToDirection(player.getX() - 1, player.getY());
    }

    @Override
    default void onExecution(Player player, Position start, Position end) {
        player.face(start);
        World.schedule(new Task(true, 1) {
            int ticks = 0;
            @Override
            public void execute() {
                switch (ticks++) {
                    case 1:
                        player.animate(new Animation(2586));
                        break;
                    case 2:
                        player.animate(new Animation(2588));
                        player.move(new Position(start.getX() - 2, start.getY(), 1));
                        break;
                    case 4:
                        player.move(end);
                        player.animate(new Animation(2588));
                        cancel();
                        break;
                }
            }
        });
//        TaskQueue.queue(new Task(player, 1, true) {
//            int ticks = 0;
//            @Override
//            public void execute() {
//                switch (ticks++) {
//                    case 1:
//                        player.getUpdateFlags().sendAnimation(new Animation(2586));
//                        break;
//                    case 2:
//                        player.getUpdateFlags().sendAnimation(new Animation(2588));
//                        player.teleport(new Location(start.getX() - 2, start.getY(), 2));
//                        break;
//                    case 4:
//                        player.teleport(end);
//                        player.getUpdateFlags().sendAnimation(new Animation(2588));
//                        stop();
//                        break;
//                }
//            }
//        });
    }

    @Override
    default void onCancellation(Player player) {
    }
}
