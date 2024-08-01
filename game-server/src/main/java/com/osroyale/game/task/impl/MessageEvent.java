package com.osroyale.game.task.impl;

import com.osroyale.Config;
import com.osroyale.content.triviabot.TriviaBot;
import com.osroyale.game.task.Task;
import com.osroyale.game.world.World;
import com.osroyale.net.discord.Discord;
import com.osroyale.util.Utility;

/**
 * Sends game messages to all the online players.
 *
 * @author Daniel
 */
public class MessageEvent extends Task {

    /** The message randomevent ticks. */
    private int tick;

    /** Constructs a new <code>MessageEvent</code>. */
    public MessageEvent() {
        super(180);
        this.tick = 0;
    }

    @Override
    public void execute() {
        tick++;

        if (tick % 2 == 0) {
            String message = Utility.randomElement(Config.MESSAGES);
            World.sendMessage("<img=15> <col=2C7526>Broadcast: </col>" + message);
        } else {
            TriviaBot.assign();
        }
    }
}
