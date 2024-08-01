package com.osroyale.game.task.impl;

import com.osroyale.game.task.Task;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.object.GameObject;

public abstract class SteppingStoneTask extends Task {

    private final Player player;
    private final GameObject object;
    protected int tick;

    public SteppingStoneTask(Player player, GameObject object) {
        super(true, 0);
        this.player = player;
        this.object = object;
    }

    @Override
    public void onSchedule() {
        if (!player.getPosition().isWithinDistance(object.getPosition(), 1)) {
            cancel();
            return;
        }
    }

    public abstract void onExecute();

    @Override
    public void execute() {
        onExecute();
        tick++;
    }

    @Override
    public void onCancel(boolean logout) {

    }

}
