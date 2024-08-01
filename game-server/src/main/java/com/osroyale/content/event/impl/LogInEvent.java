package com.osroyale.content.event.impl;

import com.osroyale.content.event.InteractionEvent;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 16-6-2017.
 */
public final class LogInEvent extends InteractionEvent {

    public LogInEvent() {
        super(InteractionType.LOG_IN);
    }
}
