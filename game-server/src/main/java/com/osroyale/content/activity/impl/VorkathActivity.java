package com.osroyale.content.activity.impl;

import com.osroyale.content.ActivityLog;
import com.osroyale.content.achievement.AchievementHandler;
import com.osroyale.content.achievement.AchievementKey;
import com.osroyale.content.activity.Activity;
import com.osroyale.content.activity.ActivityDeathType;
import com.osroyale.content.activity.ActivityType;
import com.osroyale.content.event.impl.NpcInteractionEvent;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.npc.NpcDeath;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.position.Area;
import com.osroyale.game.world.position.Position;
import com.osroyale.util.Utility;

/**
 * The activity for Vorkath.
 *
 * @author Daniel.
 */
public class VorkathActivity extends Activity {

    /** The player instance for the activity. */
    private final Player player;

    /** The Vorkath npc instance. */
    private Npc vorkath = new Npc(8059, new Position(2269, 4062));

    /** Flag if Vorkath has been summoned. */
    private boolean summoned;

    /** Constructs a new <code>VorkathActivity</code>. */
    private VorkathActivity(Player player, int instance) {
        super(1, instance);
        this.player = player;
    }

    /** Creates a new Vorkath activity for the player. */
    public static VorkathActivity create(Player player) {
        VorkathActivity activity = new VorkathActivity(player, player.playerAssistant.instance());
        activity.pause();
        activity.add(player);
        activity.add(activity.vorkath);
        activity.vorkath.blockInteract = true;
        activity.vorkath.blockFace = true;
        activity.vorkath.locking.lock();
        return activity;
    }

    @Override
    protected void start() {
    }

    @Override
    public void finish() {
        boolean successfull = vorkath.isDead();

        cleanup();
        remove(player);

        if (successfull) {
            player.activityLogger.add(ActivityLog.VORKATH);
            AchievementHandler.activate(player, AchievementKey.VORKATH);
            player.message("Congratulations, you have killed the Vorkath. Fight duration: @red@" + Utility.getTime(player.gameRecord.end(ActivityType.VORKATH)) + "</col>.");


            restart(10, () -> {
                if (Area.inVorkath(player)) {
                    create(player);
                } else {
                    remove(player);
                }
            });
        }
    }

    @Override
    public void cleanup() {
        remove(vorkath);
    }

    @Override
    protected boolean clickNpc(Player player, NpcInteractionEvent event) {
        if (event.getOpcode() == 0 && event.getNpc().id == 8059 && !summoned) {
            summoned = true;
            player.animate(827);
            vorkath.animate(7950);

            World.schedule(7, () -> {
                vorkath.transform(8060, true);
                World.schedule(1, () -> {
                    vorkath.face(player);
                    vorkath.blockInteract = false;
                    vorkath.blockFace = false;
                    vorkath.locking.unlock();
                    player.gameRecord.start();
                });

            });
            return true;
        }
        return false;
    }

    @Override
    public void onLogout(Player player) {
        cleanup();
        remove(player);
    }

    @Override
    public void onDeath(Mob mob) {
        if (mob.isNpc() && mob.getNpc().equals(vorkath)) {
            World.schedule(new NpcDeath(mob.getNpc(), this::finish));
            return;
        }

        super.onDeath(mob);
    }

    @Override
    public void onRegionChange(Player player) {
        if (!Area.inVorkath(player)) {
            cleanup();
            remove(player);
        }
    }

    @Override
    public ActivityDeathType deathType() {
        return ActivityDeathType.PURCHASE;
    }

    @Override
    public boolean canTeleport(Player player) {
        return true;
    }

    @Override
    public ActivityType getType() {
        return ActivityType.VORKATH;
    }
}
