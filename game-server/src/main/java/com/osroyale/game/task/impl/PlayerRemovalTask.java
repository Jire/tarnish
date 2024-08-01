package com.osroyale.game.task.impl;

import com.osroyale.content.activity.Activity;
import com.osroyale.content.activity.impl.duelarena.DuelArenaActivity;
import com.osroyale.content.combat.cannon.CannonManager;
import com.osroyale.game.task.TickableTask;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.persist.PlayerSerializer;
import com.osroyale.util.Stopwatch;

import java.util.concurrent.TimeUnit;

public class PlayerRemovalTask extends TickableTask {

    private final Player player;
    private final Stopwatch stopwatch = Stopwatch.start();
    private final boolean force;
    private boolean flag;

    public PlayerRemovalTask(Player player, boolean force) {
        super(true, 0);
        this.player = player;
        this.force = force;
    }

    @Override
    protected void tick() {

        // wait till player death task is finished
        if (player.isDead()) {
            return;
        }

        // player is in combat for too long
        if (player.getCombat().isUnderAttack() && stopwatch.elapsedTime(TimeUnit.SECONDS) < 60) {
            return;
        }

        // if a player is in an activity they can logout if the activity lets them or unless 60 seconds has elapsed
        if (Activity.evaluate(player, it -> !it.canLogout(player)) && (Activity.search(player, DuelArenaActivity.class).isPresent() || stopwatch.elapsedTime(TimeUnit.SECONDS) < 60)) {
            return;
        }

        if (!flag) {
            CannonManager.logout(player);
            PlayerSerializer.save(player);
            flag = true;
            return;
        }

        // make sure the players account has saved first, player saving is on a different thread so its gonna take
        // a few seconds
        if (player.saved.get()) {
            cancel();
        }

    }

    @Override
    public void onCancel(boolean logout) {
        player.unregister();
    }

}
