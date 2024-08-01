package com.osroyale.game.engine.sync.task;

import com.osroyale.game.world.entity.mob.npc.Npc;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class NpcPostUpdateTask extends SynchronizationTask {

	private static final Logger logger = LogManager.getLogger();

	private final Npc npc;

	public NpcPostUpdateTask(Npc npc) {
		this.npc = npc;
	}

	@Override
	public void run() {
		try {
			npc.updateFlags.clear();
			npc.resetAnimation();
			npc.resetGraphic();
			npc.clearTeleportTarget();
			npc.positionChange = false;
			npc.regionChange = false;
			npc.teleportRegion = false;
		} catch (Exception ex) {
			logger.error(String.format("Error in %s", NpcPostUpdateTask.class.getSimpleName()), ex);
		}
	}

}
