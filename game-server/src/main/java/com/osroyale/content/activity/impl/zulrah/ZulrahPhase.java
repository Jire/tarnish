package com.osroyale.content.activity.impl.zulrah;

import com.osroyale.game.Animation;
import com.osroyale.game.Projectile;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.task.impl.ObjectPlacementEvent;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.object.CustomGameObject;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.out.SendCameraReset;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.RandomUtils;
import com.osroyale.util.Utility;

import java.util.concurrent.TimeUnit;

/**
 * Handles all the activity phases for zulrah.
 *
 * @author Daniel
 */
public enum ZulrahPhase implements PhaseInterface<ZulrahActivity> {
    INITIALIZATION() {
        @Override
        public void execute(ZulrahActivity activity) {
            Player player = activity.player;
            int count = activity.count;

            if (count == 0) {
                player.send(new SendCameraReset());
            } else if (count == 1) {
                activity.zulrah = new Npc(2042, new Position(2266, 3073));
                activity.add(activity.zulrah);
                activity.zulrah.definition.setRetaliate(false);
                activity.zulrah.definition.setAggressive(false);
                activity.zulrah.canAttack = false;
                activity.zulrah.blockInteract = true;
                activity.zulrah.owner = player;
                activity.zulrah.face(player.getPosition());
                activity.zulrah.animate(5071);
                activity.cooldown(1);
            } else if (count == 2) {
                activity.zulrah.blockInteract = false;
                activity.zulrah.resetFace();
                activity.count = -1;
                activity.phase = INITIAL_CLOUDS;
                activity.cooldown(3);
                return;
            }

            activity.increment();
        }
    },
    INITIAL_CLOUDS() {
        @Override
        public void execute(ZulrahActivity activity) {
            Npc zulrah = activity.zulrah;

            int index = activity.count / 3;
            int stage = activity.count % 3;

            if (index >= ZulrahConstants.CLOUD_POSITIONS.length) {
                activity.count = 0;
                activity.phase = DIVE;
                activity.reset();
                activity.cooldown(2);
                return;
            }

            if (stage == 0) {
                zulrah.face(ZulrahConstants.CLOUD_POSITIONS[index]);
            } else if (stage == 1) {
                zulrah.animate(new Animation(5069, UpdatePriority.VERY_HIGH));
                World.sendProjectile(zulrah, ZulrahConstants.CLOUD_POSITIONS[index], new Projectile(1045, 1, 35, 85, 40));
            } else if (stage == 2) {
                CustomGameObject cloud = new CustomGameObject(11700, activity.getInstance(), ZulrahConstants.CLOUD_POSITIONS[index]);
                World.schedule(new ObjectPlacementEvent(cloud, 35, () -> activity.clouds.remove(cloud)));
                activity.clouds.add(cloud);
            }

            activity.increment();
        }
    },
    DIVE() {
        @Override
        public void execute(ZulrahActivity activity) {
            Npc zulrah = activity.zulrah;
            int count = activity.count;

            if (count == 0) {
                zulrah.animate(new Animation(5072, UpdatePriority.VERY_HIGH), true);
                activity.player.send(new SendMessage("Zulrah dives into the swamp..."));
            } else if (count == 3) {
                zulrah.setVisible(false);
                zulrah.transform(activity.nextForm());
                zulrah.move(RandomUtils.randomExclude(ZulrahConstants.LOCATIONS, zulrah.getPosition()));
            } else if (count == 5) {
                zulrah.locking.unlock();
                zulrah.setVisible(true);
                zulrah.animate(new Animation(5071, UpdatePriority.VERY_HIGH));
                activity.count = -1;
                activity.phase = activity.nextPhase();
                activity.reset();
                activity.cooldown(2);
                return;
            }

            activity.increment();
        }
    },
    ATTACKING() {
        @Override
        public void execute(ZulrahActivity activity) {
            Npc zulrah = activity.zulrah;

            if (!zulrah.definition.isAggressive() || !zulrah.definition.isRetaliate() || !zulrah.canAttack) {
                zulrah.definition.setAggressive(true);
                zulrah.canAttack = true;
                zulrah.definition.setRetaliate(true);
            }

            if (activity.stopwatch.elapsedTime(TimeUnit.SECONDS) >= 10 && Utility.random(5) == 0) {
                activity.reset();
                activity.phase = DIVE;
                zulrah.definition.setAggressive(false);
                activity.zulrah.definition.setRetaliate(false);
                zulrah.canAttack = false;
                activity.cooldown(2);
                return;
            }

            activity.increment();
            activity.cooldown(2);
        }
    },
    POISONOUS_CLOUD() {
        @Override
        public void execute(ZulrahActivity activity) {
            Npc zulrah = activity.zulrah;

            if (activity.target == null) {
                activity.target = activity.getCloudPosition();
            }

            if (activity.clouds.size() >= ZulrahConstants.MAXIMUM_CLOUDS) {
                activity.reset();
                activity.phase = DIVE;
                activity.cooldown(2);
                return;
            }

            int stage = activity.count % 3;

            if (stage == 0) {
                zulrah.face(activity.target);
            } else if (stage == 1) {
                zulrah.animate(new Animation(5069, UpdatePriority.VERY_HIGH));
                World.sendProjectile(zulrah, activity.target, new Projectile(1045, 1, 35, 85, 40));
            } else if (stage == 2) {
                CustomGameObject cloud = new CustomGameObject(11700, activity.getInstance(), activity.target);
                activity.clouds.add(cloud);
                World.schedule(new ObjectPlacementEvent(cloud, 150, () -> activity.clouds.remove(cloud)));
                activity.target = null;
            }

            activity.increment();
            activity.cooldown(1);
        }
    },
    SNAKELINGS() {
        @Override
        public void execute(ZulrahActivity activity) {
            Npc zulrah = activity.zulrah;

            if (activity.target == null) {
                activity.target = activity.getSnakelingPosition();
            }

            if (activity.snakes.size() >= ZulrahConstants.MAXIMUM_SNAKELINGS) {
                activity.reset();
                activity.phase = DIVE;
                activity.cooldown(2);
                return;
            }

            int stage = activity.count % 3;

            if (stage == 0) {
                zulrah.face(activity.target);
            } else if (stage == 1) {
                zulrah.animate(new Animation(5068, UpdatePriority.VERY_HIGH));
                World.sendProjectile(zulrah, activity.target, new Projectile(1047, 1, 35, 85, 40));
            } else if (stage == 2) {
                Npc snake = new Npc(2045, activity.target);
                activity.add(snake);
                snake.getCombat().attack(activity.player);
                activity.snakes.add(snake);
                activity.target = null;
            }

            activity.increment();
            activity.cooldown(1);
        }
    },
}
