package com.osroyale.game.engine.sync.task;

import com.osroyale.game.world.entity.mob.npc.Npc;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ParallelNpcPreUpdateTask extends SynchronizationTask {

    private static final Logger logger = LogManager.getLogger();

    private final Npc npc;

    public ParallelNpcPreUpdateTask(Npc npc) {
        this.npc = npc;
    }

    @Override
    public void run() {
        try {
            if (npc.atomicPlayerCount.get() == 0) {
                return;
            }

            if (npc.regionChange) {
                npc.lastPosition = npc.getPosition();
            }

            npc.movement.processNextMovement();
            npc.sequence();
        } catch (Exception ex) {
            logger.fatal(String.format("Error in %s. npc=%d %s", NpcPreUpdateTask.class.getSimpleName(), npc.id, npc.getPosition()),  ex);
        }
    }

}
