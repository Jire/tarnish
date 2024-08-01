package com.osroyale.game.event.listener;

import com.osroyale.game.event.Event;
import com.osroyale.game.event.impl.log.LogEvent;

public final class WorldEventListener implements EventListener {

    @Override
    public void accept(Event event) {
        if (event instanceof LogEvent) {
            LogEvent logEvent = (LogEvent) event;
            logEvent.log();
        }
    }

}
