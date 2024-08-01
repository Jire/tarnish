package com.osroyale.game.task.impl;

import com.osroyale.game.task.TickableTask;
import com.osroyale.game.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class SystemUpdateEvent extends TickableTask {

    private static final Logger logger = LogManager.getLogger();
    private final int ticks;

    public SystemUpdateEvent(int ticks) {
        super(true, 10);
        this.ticks = ticks;
    }

    @Override
    protected void tick() {
        int remaining = ticks - tick * 10;
        logger.info("Server shutdown in " + remaining + " ticks (" + remaining * 3 / 5 + " seconds)");

        if (remaining == 10)
            World.save();

        if (remaining == 0)
            cancel();
    }

    @Override
    public void onCancel(boolean logout) {
        World.shutdown();
    }

}
