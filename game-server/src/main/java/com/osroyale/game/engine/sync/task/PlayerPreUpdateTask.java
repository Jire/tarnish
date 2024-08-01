package com.osroyale.game.engine.sync.task;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.session.GameSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jire.tarnishps.event.Events;

public final class PlayerPreUpdateTask extends SynchronizationTask {

	private static final Logger logger = LogManager.getLogger();

	private final Player player;

	public PlayerPreUpdateTask(Player player) {
		this.player = player;
	}

	@Override
	public void run() {
		try {
			try {
				player.getSession().ifPresent(GameSession::processClientPackets);
			} catch (Exception ex) {
				logger.error(String.format("error GameSession::processClientPackets: %s", player), ex);
			}
			try {
				final Events events = player.getEvents();
				events.process(player);
			} catch (final Exception e) {
				logger.error("error Events::process: " + player, e);
			}
			try {
				player.movement.processNextMovement();
			} catch (Exception ex) {
				logger.error(String.format("error player.movement.processNextMovement(): %s", player), ex);
			}

//				elapsed = stopwatch.elapsedTime(TimeUnit.MILLISECONDS);
//				if (elapsed > 0) {
//					System.out.println(String.format("processNextMovement: %d", elapsed));
//				}
//				stopwatch.reset();



//				elapsed = stopwatch.elapsedTime(TimeUnit.MILLISECONDS);
//				if (elapsed > 0) {
//					System.out.println(String.format("sequence: %d", elapsed));
//				}
//				stopwatch.reset();
		} catch (Exception ex) {
			logger.error(String.format("Error in %s.", PlayerPreUpdateTask.class.getSimpleName()), ex);
		}
	}

}
