package com.osroyale.game.event.impl;

import com.osroyale.game.event.Event;
import com.osroyale.game.world.entity.mob.player.command.CommandParser;

public class CommandEvent implements Event {

    private final CommandParser parser;

    public CommandEvent(CommandParser parser) {
        this.parser = parser;
    }

    public CommandParser getParser() {
        return parser;
    }

}
