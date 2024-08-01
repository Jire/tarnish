package com.osroyale.content.skill.impl.agility.obstacle.impl;

import com.osroyale.content.skill.impl.agility.obstacle.ObstacleInteraction;
import com.osroyale.game.task.Task;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.object.ObjectDirection;
import com.osroyale.game.world.position.Position;
import com.osroyale.game.world.object.GameObject;
import com.osroyale.net.packet.out.SendMessage;

public interface WildernessDoorInteraction extends ObstacleInteraction {


    @Override
    default void start(Player player) {
    }

    @Override
    default boolean canExecute(Player player, int level) {
        if (player.skills.getLevel(Skill.AGILITY) < level) {
            player.dialogueFactory.sendStatement("You need an agility level of " + level + " to do this!").execute();
            return false;
        }
        GameObject object = player.attributes.get("AGILITY_OBJ", GameObject.class);
        return object != null && object.getDirection() == ObjectDirection.SOUTH;
    }

    @Override
    default void onExecution(Player player, Position start, Position end) {
        boolean entering = player.getPosition().equals(new Position(2998, 3916));
        Position destination = entering ? new Position(2998, 3930) : new Position(2998, 3917);
        Position finalDestination = entering ? new Position(2998, 3931) : new Position(2998, 3916);
//        GameObject enterDoor = player.getRegion().getGameObject(new Position(2998, 3917));
//        GameObject leavingDoor1 = player.getRegion().getGameObject(new Position(2997, 3931));
//        GameObject leavingDoor2 = player.getRegion().getGameObject(new Position(2998, 3931));

        player.face(finalDestination);

        World.schedule(new Task(2) {
            int count = 0;

            @Override
            public void execute() {
                if (player.getPosition().equals(destination)) {
                    player.mobAnimation.reset();
//                    if (entering) {
//                        leavingDoor1.rotate(Direction.EAST);
//                        leavingDoor2.rotate(Direction.SOUTH);
//                    } else {
//                        enterDoor.rotate(Direction.EAST);
//                    }
                    player.movement.walk(finalDestination);
                    return;
                }
                if (player.getPosition().equals(finalDestination)) {
                    if (entering) {
//                        leavingDoor1.rotate(Direction.WEST);
//                        leavingDoor2.rotate(Direction.WEST);
                        player.send(new SendMessage("Welcome the the wilderness agility course. Be careful :)"));
                    } else {
//                        enterDoor.rotate(Direction.WEST);
                    }
                    cancel();
                    return;
                }
                switch (count) {
                    case 0:
//                        if (entering) {
//                            enterDoor.rotate(Direction.EAST);
//                        } else {
//                            leavingDoor1.rotate(Direction.EAST);
//                            leavingDoor2.rotate(Direction.SOUTH);
//                        }
                        player.movement.walk(destination);
                        break;
                    case 1:
                        player.mobAnimation.setWalk(762);
                        player.mobAnimation.setStand(762);
                        break;
                    case 2:
//                        if (entering) {
//                            enterDoor.rotate(Direction.WEST);
//                        } else {
//                            leavingDoor1.rotate(Direction.WEST);
//                            leavingDoor2.rotate(Direction.WEST);
//                        }
                        break;
                }
                count++;
            }
        });
    }

    @Override
    default void onCancellation(Player player) {
    }
}