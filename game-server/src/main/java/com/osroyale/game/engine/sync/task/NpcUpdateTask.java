package com.osroyale.game.engine.sync.task;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.SendNpcUpdate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NpcUpdateTask extends SynchronizationTask {

    private static final Logger logger = LogManager.getLogger();

    private final Player player;

    public NpcUpdateTask(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        try {
            if (player == null) {
                return;
            }

            player.send(new SendNpcUpdate());
        } catch (Exception ex) {
            logger.fatal(String.format("Error in %s %s", this.getClass().getSimpleName(), player), ex);
        }
    }

}
