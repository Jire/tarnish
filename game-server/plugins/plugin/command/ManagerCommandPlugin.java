package plugin.command;

import com.osroyale.content.dialogue.DialogueFactory;
import com.osroyale.game.plugin.extension.CommandExtension;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.entity.mob.player.command.Command;
import com.osroyale.game.world.entity.mob.player.command.CommandParser;
import com.osroyale.net.discord.DiscordPlugin;

public class ManagerCommandPlugin extends CommandExtension {

    @Override
    public void register() {
        /* Broadcasts a message to the entire server */
        commands.add(new Command("broadcast") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    final String message = parser.nextLine();
                    World.sendBroadcast(1, message, false);
                    DiscordPlugin.sendAnnouncement(message);
                }
            }
        });

        /* Sends a discord message from the bot. */
        commands.add(new Command("discord") {
            //https://discord.com/api/oauth2/authorize?client_id=1048812219212767243&permissions=67584&scope=bot
            @Override
            public void execute(Player player, CommandParser parser) {
                    DiscordPlugin.sendSuggestion("settings", "die");
            }
        });

        /* Promotes a player. */
        commands.add(new Command("promote") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    StringBuilder name = new StringBuilder(parser.nextString());
                    while (parser.hasNext()) {
                        name.append(" ").append(parser.nextString());
                    }
                    World.search(name.toString()).ifPresent(other -> {
                        DialogueFactory factory = player.dialogueFactory;
                        factory.sendOption("Helper", () -> {
                            other.right = PlayerRight.HELPER;
                            player.message("You have promoted " + other.getName() + ": " + other.right.getName());
                            other.message("You have been promoted: " + other.right.getName());
                        }, "Moderator", () -> {
                            other.right = PlayerRight.MODERATOR;
                            player.message("You have promoted " + other.getName() + ": " + other.right.getName());
                            other.message("You have been promoted: " + other.right.getName());
                        }, "Admin", () -> {
                            other.right = PlayerRight.ADMINISTRATOR;
                            player.message("You have promoted " + other.getName() + ": " + other.right.getName());
                            other.message("You have been promoted: " + other.right.getName());
                        }).execute();
                    });
                } else {
                    player.message("Invalid command use; ::promote settings");
                }
            }
        });
        commands.add(new Command("master") {

            @Override
            public void execute(Player player, CommandParser parser) {
                player.skills.master();

            }
        });

        /* Removes current slayer task from player. */
        commands.add(new Command("removeslayertask", "removetask") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    StringBuilder name = new StringBuilder(parser.nextString());
                    while (parser.hasNext()) {
                        name.append(" ").append(parser.nextString());
                    }
                    World.search(name.toString()).ifPresent(other -> {
                        other.slayer.setTask(null);
                        other.slayer.setAmount(0);
                        other.message("Your slayer task was reset.");
                    });
                } else {
                    player.message("Invalid command use; ::removetask settings");
                }
            }
        });

    }

    @Override
    public boolean canAccess(Player player) {
        return PlayerRight.isManager(player);
    }
}
