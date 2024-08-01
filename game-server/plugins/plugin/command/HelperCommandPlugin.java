package plugin.command;

import com.osroyale.Config;
import com.osroyale.content.Yell;
import com.osroyale.content.dialogue.DialogueFactory;
import com.osroyale.content.skill.impl.magic.teleport.Teleportation;
import com.osroyale.game.plugin.extension.CommandExtension;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.entity.mob.player.command.Command;
import com.osroyale.game.world.entity.mob.player.command.CommandParser;
import com.osroyale.net.packet.out.SendInputAmount;
import com.osroyale.net.packet.out.SendMessage;

import java.util.concurrent.TimeUnit;

public class HelperCommandPlugin extends CommandExtension {

    @Override
    protected void register() {
        commands.add(new Command("mute") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    StringBuilder name = new StringBuilder(parser.nextString());

                    while (parser.hasNext()) {
                        name.append(" ").append(parser.nextString());
                    }

                    World.search(name.toString()).ifPresent(other -> {
                        if (PlayerRight.isAdministrator(other) && !PlayerRight.isDeveloper(player)) {
                            player.message("You do not have permission to mute this player!");
                            return;
                        }

                        DialogueFactory factory = player.dialogueFactory;
                        factory.sendOption("Mute by day", () -> {
                            factory.onAction(() -> player.send(new SendInputAmount("How long do you want this mute to last for?", 2, input -> {
                                other.punishment.mute(Integer.parseInt(input), TimeUnit.DAYS);
                                factory.clear();
                            })));
                        }, "Mute by hour", () -> {
                            factory.onAction(() -> player.send(new SendInputAmount("How long do you want this mute to last for?", 3, input -> {
                                other.punishment.mute(Integer.parseInt(input), TimeUnit.HOURS);
                                factory.clear();
                            })));
                        }, "Mute by minute", () -> {
                            factory.onAction(() -> player.send(new SendInputAmount("How long do you want this mute to last for?", 3, input -> {
                                other.punishment.mute(Integer.parseInt(input), TimeUnit.MINUTES);
                                factory.clear();
                            })));
                        }, "Mute forever", () -> {
                            factory.onAction(() -> {
                                other.punishment.mute(9999, TimeUnit.DAYS);
                                factory.clear();
                            });
                        }).execute();
                    });
                } else {
                    player.message("Invalid command use; ::mute settings");
                }
            }
        });
        commands.add(new Command("bank") {
            @Override
            public void execute(Player player, CommandParser parser) {
                player.bank.open();
            }
        });

        commands.add(new Command("unmute") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    StringBuilder name = new StringBuilder(parser.nextString());

                    while (parser.hasNext()) {
                        name.append(" ").append(parser.nextString());
                    }

                    World.search(name.toString()).ifPresent(other -> {
                        other.punishment.unmute();
                        other.dialogueFactory.sendStatement("You have been unmuted!").execute();
                        player.message("unmute was complete");

                    });
                } else {
                    player.message("Invalid command use; ::unmute settings");
                }
            }
        });

        commands.add(new Command("jail") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    StringBuilder name = new StringBuilder(parser.nextString());

                    while (parser.hasNext()) {
                        name.append(" ").append(parser.nextString());
                    }

                    World.search(name.toString()).ifPresent(other -> {
                        if (PlayerRight.isAdministrator(other) && !PlayerRight.isDeveloper(player)) {
                            player.message("You do not have permission to jail this player!");
                            return;
                        }

                        DialogueFactory factory = player.dialogueFactory;
                        factory.sendOption("Jail by day", () -> {
                            factory.onAction(() -> player.send(new SendInputAmount("How long do you want this jail to last for?", 2, input -> {
                                other.punishment.jail(Integer.parseInt(input), TimeUnit.DAYS);
                                factory.clear();
                            })));
                        }, "Jail by hour", () -> {
                            factory.onAction(() -> player.send(new SendInputAmount("How long do you want this jail to last for?", 3, input -> {
                                other.punishment.jail(Integer.parseInt(input), TimeUnit.HOURS);
                                factory.clear();
                            })));
                        }, "Jail by minute", () -> {
                            factory.onAction(() -> player.send(new SendInputAmount("How long do you want this jail to last for?", 3, input -> {
                                other.punishment.jail(Integer.parseInt(input), TimeUnit.MINUTES);
                                factory.clear();
                            })));
                        }, "Jail forever", () -> {
                            factory.onAction(() -> {
                                other.punishment.jail(9999, TimeUnit.DAYS);
                                factory.clear();
                            });
                        }).execute();
                    });
                } else {
                    player.message("Invalid command use; ::jail settings");
                }
            }
        });

        commands.add(new Command("unjail") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    StringBuilder name = new StringBuilder(parser.nextString());

                    while (parser.hasNext()) {
                        name.append(" ").append(parser.nextString());
                    }
                    World.search(name.toString()).ifPresent(other -> {
                        other.punishment.unJail();
                        other.dialogueFactory.sendStatement("You have been unjailed!").execute();
                        player.message("unjail was complete");
                    });

                } else {
                    player.message("Invalid command use; ::unjail settings");
                }
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

        commands.add(new Command("staffzone", "sz") {
            @Override
            public void execute(Player player, CommandParser parser) {
                Teleportation.teleport(player, Config.STAFF_ZONE);
                player.send(new SendMessage("Welcome to the staffzone, " + player.getName() + "."));
            }
        });


    }

    @Override
    public boolean canAccess(Player player) {
        return PlayerRight.isHelper(player);
    }

}
