package com.osroyale.game.engine.sync.task;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.session.GameSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ParallelPlayerPreUpdateTask extends SynchronizationTask {

    private static final Logger logger = LogManager.getLogger();

    private final Player player;

    public ParallelPlayerPreUpdateTask(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        try {

        } catch (Exception ex) {
            logger.error(String.format("Error in %s. player=%s", PlayerPreUpdateTask.class.getSimpleName(), player), ex);
        }
    }

}
