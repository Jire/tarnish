package com.osroyale.game.task.impl;

import com.osroyale.game.task.TickableTask;
import com.osroyale.game.world.entity.mob.player.Player;

public class SkullRemoveTask extends TickableTask {

    private final Player player;

    private int skullTime;

    public SkullRemoveTask(Player player) {
        super(false, 0);
        this.player = player;
    }

    @Override
    protected void tick() {
        if (!player.isValid()) {
            cancel();
            return;
        }

        if (skullTime <= 0) {
            player.skulling.unskull();
            cancel();
            return;
        }

        skullTime--;
    }

    public void setSkullTime(int skullTime) {
        this.skullTime = skullTime;
    }

    public int getSkullTime() {
        return skullTime;
    }

}
