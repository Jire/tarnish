package com.osroyale.content.activity.impl;

import com.osroyale.Config;
import com.osroyale.content.activity.Activity;
import com.osroyale.content.activity.ActivityType;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;

public class JailActivity extends Activity {
    private final Player player;

    private JailActivity(Player player) {
        super(30, Mob.DEFAULT_INSTANCE);
        this.player = player;
    }

    public static JailActivity create(Player player) {
        JailActivity activity = new JailActivity(player);
        player.move(Config.JAIL_ZONE);
        activity.add(player);
        activity.resetCooldown();
        player.setVisible(true);
        return activity;
    }

    @Override
    protected void start() {
        if (!player.punishment.isJailed()) {
            finish();
        }
    }

    @Override
    public void onDeath(Mob mob) {
        player.move(Config.JAIL_ZONE);
        player.message("BAM! YOU'RE BACK!");
    }

    @Override
    public boolean canTeleport(Player player) {
        player.message("You are jailed you douche!");
        return false;
    }

    @Override
    public void onRegionChange(Player player) {
        player.move(Config.JAIL_ZONE);
    }

    @Override
    public void finish() {
        remove(player);
        player.move(Config.DEFAULT_POSITION);
        player.message("Time's up! You are free to go.");
    }

    @Override
    public void cleanup() {

    }

    @Override
    public ActivityType getType() {
        return ActivityType.JAIL;
    }
}
