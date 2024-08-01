package plugin.command;

import com.osroyale.Config;
import com.osroyale.content.Yell;
import com.osroyale.content.skill.impl.magic.teleport.Teleportation;
import com.osroyale.content.skill.impl.magic.teleport.TeleportationData;
import com.osroyale.game.plugin.extension.CommandExtension;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.entity.mob.player.command.Command;
import com.osroyale.game.world.entity.mob.player.command.CommandParser;
import com.osroyale.net.packet.out.SendMessage;


public class DonatorCommandPlugin extends CommandExtension {

    @Override
    protected void register() {
        commands.add(new Command("superdonatorzone", "sdonorzone", "sdzone", "sdz") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (PlayerRight.isElite(player) || PlayerRight.isKing(player)) {
                Teleportation.teleport(player, Config.SUPER_DONATOR_ZONE, 20, TeleportationData.DONATOR, () -> {
                    player.send(new SendMessage("Welcome to the super donator zone, " + player.getName() + "!"));
                    });
           } else {
                    player.message("You must be an elite or king donator to do this.");
                }


                commands.add(new Command("yell") {
                    @Override
                    public void execute(Player player, CommandParser parser) {
                        if (parser.hasNext()) {
                            final String message = parser.nextLine();
                            Yell.yell(player, message);
                        }
                    }
                });
            }
        });
        commands.add(new Command("donatorzone", "donorzone", "dzone", "dz") {
            @Override
            public void execute(Player player, CommandParser parser) {
                Teleportation.teleport(player, Config.REGULAR_DONATOR_ZONE, 20, TeleportationData.DONATOR, () -> {
                    player.send(new SendMessage("Welcome to the regular donator zone, " + player.getName() + "!"));
                });

                commands.add(new Command("yell") {
                    @Override
                    public void execute(Player player, CommandParser parser) {
                        if (parser.hasNext()) {
                            final String message = parser.nextLine();
                            Yell.yell(player, message);
                        }
                    }
                });
            }
        });
        commands.add(new Command("yell") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    final String message = parser.nextLine();
                    Yell.yell(player, message);
                }
            }
        });
    }

    @Override
    public boolean canAccess(Player player) {
        return PlayerRight.isDonator(player);
    }

}
