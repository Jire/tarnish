package com.osroyale.content.skill.impl.agility.obstacle;

import com.osroyale.Config;
import com.osroyale.content.achievement.AchievementHandler;
import com.osroyale.content.achievement.AchievementKey;
import com.osroyale.content.activity.randomevent.RandomEventHandler;
import com.osroyale.content.clanchannel.content.ClanTaskKey;
import com.osroyale.content.skill.impl.agility.Agility;
import com.osroyale.game.Animation;
import com.osroyale.game.task.Task;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.MobAnimation;
import com.osroyale.game.world.entity.mob.UpdateFlag;
import com.osroyale.game.world.entity.mob.data.LockType;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.ground.GroundItem;
import com.osroyale.game.world.position.Area;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.Utility;

import java.util.concurrent.TimeUnit;

public interface ObstacleInteraction {

    int getAnimation();

    String getPreMessage();

    String getPostMessage();

    default void start(Player player) {}

    void onExecution(Player player, Position start, Position end);

    default void onCancellation(Player player) {}

    default void execute(Player player, Obstacle next, Position start, Position end, int level, float experience, int ordinal) {
        if (!canExecute(player, level))
            return;

        if (player.locking.locked(LockType.MASTER))
            return;

        player.getCombat().reset();
        player.locking.lock();

        World.schedule(new Task(true, 1) {
            private final MobAnimation ANIMATION = player.mobAnimation.copy();
            private final boolean RUNNING = player.movement.isRunning();
            private boolean started = false;
            private Obstacle nextObstacle = next;

            @Override
            public void onSchedule() {
                player.movement.setRunningToggled(false);
                if (getPreMessage() != null)
                    player.send(new SendMessage(getPreMessage()));
                start(player);
                attach(player);
            }

            @Override
            public void execute() {
                if (nextObstacle != null && player.getPosition().equals(nextObstacle.getEnd())) {
                    if (canExecute(player, level)) {
                        nextObstacle.getType().getInteraction().start(player);
                        nextObstacle.getType().getInteraction().onExecution(player, nextObstacle.getStart(), nextObstacle.getEnd());
                        if (nextObstacle.getType().getInteraction().getPreMessage() != null)
                            player.send(new SendMessage(nextObstacle.getType().getInteraction().getPreMessage()));
                    }
                    if (nextObstacle.getNext() == null) {
                        this.cancel();
                        return;
                    }
                    nextObstacle.getType().getInteraction().onCancellation(player);
                    nextObstacle = nextObstacle.getNext();
                } else if (player.getPosition().equals(end)) {
                    if (nextObstacle != null) {
                        if (canExecute(player, level)) {
                            nextObstacle.getType().getInteraction().start(player);
                            nextObstacle.getType().getInteraction().onExecution(player, nextObstacle.getStart(), nextObstacle.getEnd());
                            if (nextObstacle.getType().getInteraction().getPreMessage() != null) {
                                player.send(new SendMessage(nextObstacle.getType().getInteraction().getPreMessage()));
                            }
                        }
                        if (nextObstacle.getNext() != null) {
                            nextObstacle.getType().getInteraction().onCancellation(player);
                            nextObstacle = nextObstacle.getNext();
                        } else {
                            this.cancel();
                            return;
                        }
                    } else {
                        this.cancel();
                        return;
                    }
                }
                if (!started) {
                    started = true;
                    onExecution(player, start, end);
                    if (ordinal > -1 && Utility.random(50) == 0) {

                        if (!Area.inWildernessResource(player) || !Area.inBarbarianCourse(player) || !Area.inGnomeCourse(player)) {
                            GroundItem.create(player, new Item(11849, 1));
                            player.send(new SendMessage("<col=C60DDE>There appears to be a wild Grace mark near you."));
                        }
                    }
                }
            }

            @Override
            public void onCancel(boolean logout) {
                if (logout) {
                    player.move(start);
                    return;
                }
                if (getPostMessage() != null)
                    player.send(new SendMessage(getPostMessage()));
                if (experience > 0)
                    player.skills.addExperience(Skill.AGILITY, experience * 25);
                if (ordinal > -1) {
                    if (ordinal == 0) {
                        player.attributes.set("AGILITY_FLAGS", 1 << ordinal);
                    } else {
                        int i = player.attributes.get("AGILITY_FLAGS", Integer.class) | (1 << ordinal);
                        player.attributes.set("AGILITY_FLAGS", i);
                    }
                }

                if (Area.inGnomeCourse(player)) {
                    rewards(player, "Gnome Agility", Agility.GNOME_FLAGS, 39);
                } else if (Area.inBarbarianCourse(player)) {
                    rewards(player, "Barbarian Agility", Agility.BARBARIAN_FLAGS, 46.5f);
                } else if (Area.inWildernessCourse(player)) {
                    rewards(player, "Wilderness Agility", Agility.WILDERNESS_FLAGS, 498.9f);
                } else if (Area.inSeersCourse(player)) {
                    rewards(player, "Seers Agility", Agility.SEERS_FLAGS, 435);
                } else if (Area.inArdougneCourse(player)) {
                    rewards(player, "Ardougne Agility", Agility.ARDOUGNE_FLAGS, 529);
                }

                player.mobAnimation = ANIMATION;
                player.animate(new Animation(65535));
                player.updateFlags.add(UpdateFlag.APPEARANCE);
                player.movement.setRunningToggled(RUNNING);
                Position nextPosition = nextObstacle != null ? nextObstacle.getEnd() : end;
                if (!player.getPosition().equals(nextPosition)) {
                    player.move(nextPosition);
                } else {
                    int time = player.attributes.get("AGILITY_TYPE", ObstacleType.class) == ObstacleType.WILDERNESS_COURSE ? 1199 : 599;
                    player.locking.lock(time, TimeUnit.MILLISECONDS, LockType.MASTER);
                }
                onCancellation(player);
            }
        });
    }

    default boolean canExecute(Player player, int level) {
        if (player.skills.getLevel(Skill.AGILITY) < level) {
            player.dialogueFactory.sendStatement("You need an agility level of " + level + " to do this!").execute();
            return false;
        }
        return true;
    }

    default boolean rewards(Player player, String course, int flags, float bonus) {
        int flag = player.attributes.get("AGILITY_FLAGS", Integer.class);
        if ((flag & flags) != flags) {
            return false;
        }

        player.skills.addExperience(Skill.AGILITY, bonus * Config.AGILITY_MODIFICATION);
        player.send(new SendMessage("You have completed the " + course + " course and receive 5 tickets."));
        player.forClan(channel -> channel.activateTask(ClanTaskKey.AGILITY_COURSE, player.getName()));
        player.playerAssistant.activateSkilling(1);
        RandomEventHandler.trigger(player);
        AchievementHandler.activate(player, AchievementKey.AGILITY, 1);
        player.inventory.addOrDrop(new Item(2996, 5));
        player.attributes.set("AGILITY_FLAGS", 0);
        return true;
    }

}