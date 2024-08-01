package plugin.command;

import com.osroyale.game.plugin.extension.CommandExtension;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.entity.mob.player.command.Command;
import com.osroyale.game.world.entity.mob.player.command.CommandParser;
import com.osroyale.net.packet.out.SendItemOnInterface;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendScrollbar;
import com.osroyale.net.packet.out.SendString;
import plugin.itemon.RottenTomatoePlugin;

import java.util.ArrayList;
import java.util.List;

public class ModeratorCommandPlugin extends CommandExtension {

    @Override
    protected void register() {
        commands.add(new Command("onlineplayers", "playerlist") {
            @Override
            public void execute(Player player, CommandParser parser) {
                List<Player> staffs = new ArrayList<>();
                for (Player other : World.getPlayers()) {
                    staffs.add(other);
                }
                int length = staffs.size() < 25 ? 25 : staffs.size();
                player.send(new SendString("", 37113));
                player.send(new SendString("", 37107));
                player.send(new SendString("Tarnish Online Players", 37103));
                player.send(new SendScrollbar(37110, length * 20));
                for (int index = 0, string = 37111; index < length; index++, string++) {
                    if (index < staffs.size()) {
                        Player staff = staffs.get(index);
                        player.send(new SendString(PlayerRight.getCrown(staff) + " " + staff.getName() + "    (<col=255>" + staff.right.getName() + "</col>)", string));
                    } else {
                        player.send(new SendString("", string));
                    }
                }
                player.send(new SendItemOnInterface(37199));
                player.interfaceManager.open(37100);
            }
        });

        commands.add(new Command("manage", "mute", "unmute", "jail", "unjail", "kick") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (!parser.hasNext()) {
                    player.message("Invalid command use; ::manage settings");
                    return;
                }

                StringBuilder name = new StringBuilder(parser.nextString());

                while (parser.hasNext()) {
                    name.append(" ").append(parser.nextString());
                }

                World.search(name.toString()).ifPresent(other -> {
                    if (PlayerRight.isAdministrator(other) && !PlayerRight.isDeveloper(player)) {
                        player.message("Nice try bitch!");
                    }   else {
                    RottenTomatoePlugin.open(player, other);
                     }
                });
            }
        });

        commands.add(new Command("teletome", "t2m", "tele2m") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {

                    final String name = parser.nextLine();

                    if (World.search(name).isPresent()) {
                        final Player target = World.search(name).get();
                        if (target.isBot) {
                            player.send(new SendMessage("You can't teleport bot to you!"));
                            return;
                        }

                        if (target.getCombat().inCombat() && !PlayerRight.isDeveloper(player)) {
                            player.message("That player is currently in combat!");
                            return;
                        }

                        target.move(player.getPosition());
                        target.instance = player.instance;
                        target.pvpInstance = player.pvpInstance;
                    } else {
                        player.send(new SendMessage("The player '" + name + "' either doesn't exist, or is offline."));
                    }
                }
            }
        });

        commands.add(new Command("teleto", "t2") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {

                    final String name = parser.nextLine();

                    if (World.search(name).isPresent()) {
                        final Player target = World.search(name).get();
                        player.move(target.getPosition());
                        player.instance = target.instance;
                        player.pvpInstance = target.pvpInstance;
                    } else {
                        player.send(new SendMessage("The player '" + name + "' either doesn't exist, or is offline."));
                    }
                }
            }
        });
    }

    @Override
    public boolean canAccess(Player player) {
        return PlayerRight.isModerator(player);
    }

}
