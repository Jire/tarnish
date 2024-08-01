package com.osroyale.net.discord;

import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.util.Stopwatch;
import com.osroyale.util.Utility;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * The discord dispatcher.
 *
 * @author Daniel
 */
public final class DiscordDispatcher implements Consumer<MessageCreateEvent> {

    public static final String COMMAND_PREFIX = "::";

    private final Stopwatch uptime;

    public DiscordDispatcher(Stopwatch uptime) {
        this.uptime = uptime;
    }

    @Override
    public void accept(final MessageCreateEvent event) {
        final Message message = event.getMessage();
        final String content = message.getContent();
        if (!content.startsWith(COMMAND_PREFIX)) {
            return;
        }
        final String command = content.substring(COMMAND_PREFIX.length());
        final MessageChannel channel = Objects.requireNonNull(message.getChannel().block());

        switch (command) {
            case "commands": {
                String builder = ":uptime - displays the current server uptime | "
                        + "::players - displays the server's current player count | "
                        + "::staffonline - displays the server's current staff count | ";
                channel.createMessage(builder).block();
                break;
            }
            case "uptime":
                channel.createMessage("OS Royale has been up for " + Utility.getUptime(uptime) + "!").block();
                break;
            case "players":
                channel.createMessage("There are currently " + World.getPlayerCount() + " players online!").block();
                break;
            case "staffonline": {
                List<Player> staffs = World.getStaff();
                if (staffs.isEmpty()) {
                    channel.createMessage("There are no staff members online! Don't get any ideas, I am always watching :)").block();
                    return;
                }

                StringBuilder builder = new StringBuilder();

                for (int index = 0; index < staffs.size(); index++) {
                    Player staff = staffs.get(index);
                    String string = staff.getName() + " (" + staff.right.getName() + ")" + (index == (staffs.size() - 1) ? "" : ", ");
                    builder.append(string);
                }

                channel.createMessage(builder.toString()).block();
                break;
            }
        }
    }

}
