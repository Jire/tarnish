package com.osroyale.net.discord;


import com.osroyale.game.world.World;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

/**
 *
 * Discord.java | 8:06:53 AM
 */
public class BotListener implements EventListener {

    @Override
    public void onEvent(@NotNull GenericEvent genericEvent) {
        if (DiscordPlugin.getJDA() == null) {
            System.out.println("Returning JDA FAIL");
            return;
        }
        if (genericEvent instanceof MessageReceivedEvent) { // handles messages recieved
            MessageReceivedEvent msg = (MessageReceivedEvent) genericEvent;
            String message = msg.getMessage().getContentDisplay();
            /**
             * Calling the command type is correct prefix
             */
            if (message.startsWith(Constants.COMMAND_PREFIX)) { // commands
                MessageReceivedEvent messageEvent = (MessageReceivedEvent) genericEvent;
                String command = message.substring(Constants.COMMAND_PREFIX.length()).toLowerCase();
                String[] cmd = command.split(" ");
                switch (cmd[0]) {
                    /**
                     * Handles player specific command types
                     */
                    case "players":
                        messageEvent.getChannel().sendMessage("There is currently " + World.getPlayers() + " players online.").queue();
                        break;

                    case "pollyn":
                        String question = command.substring(7);
                        DiscordPlugin.pollYN(question);
                        break;
                    /**
                     * Handles support commands
                     */

                    /**
                     * Handles moderator commands
                     */

                    /**
                     * Handles admin commands
                     */

                    /**
                     * Handles owner commands
                     */
                    case "updatelog":
                        if (messageEvent.getMember().isOwner()) {
                            DiscordPlugin.sendUpdateMessage("Jack");
                        }
                        break;
                }
            }
        }
    }

}

