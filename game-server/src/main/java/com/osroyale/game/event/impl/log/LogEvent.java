package com.osroyale.game.event.impl.log;

import com.osroyale.Config;
import com.osroyale.game.event.Event;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;

public abstract class LogEvent implements Event {

    private static final Logger logger = LogManager.getLogger();
    protected final LocalDateTime dateTime = LocalDateTime.now();

    public void log() {
        if (!Config.FORUM_INTEGRATION || !Config.LOG_PLAYER) {
            return;
        }

        Thread.startVirtualThread(() -> {
            try {
                onLog();
            } catch (Exception ex) {
                logger.error(String.format("Error logging %s", this.getClass().getSimpleName()), ex);
            }
        });
    }

    public abstract void onLog() throws Exception;

}
