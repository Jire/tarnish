package plugin.command;

import com.osroyale.Config;
import com.osroyale.content.skill.impl.magic.Spellbook;
import com.osroyale.game.plugin.extension.CommandExtension;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.Direction;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.data.LockType;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.entity.mob.player.command.Command;
import com.osroyale.game.world.entity.mob.player.command.CommandParser;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.ItemDefinition;
import com.osroyale.game.world.items.containers.ItemContainer;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.out.SendItemOnInterface;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendScrollbar;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.util.MessageColor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AdminCommandPlugin extends CommandExtension {

    @Override
    public void register() {
        commands.add(new Command("pnpc") {
            @Override
            public void execute(Player player, CommandParser parser) {
                player.playerAssistant.transform(parser.nextInt());
            }
        });

        commands.add(new Command("spawnnpc") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    int id = parser.nextInt();
                    Npc npc = new Npc(id, player.getPosition(), Config.NPC_WALKING_RADIUS, Mob.DEFAULT_INSTANCE, Direction.NORTH);
                    npc.register();
                    npc.locking.lock(LockType.MASTER);
                    Path path = Paths.get("./data/def/npc/npc_spawns.json");

                    try {
                        if (!Files.exists(path)) {
                            Files.createFile(path);
                        }
                        FileWriter fileWriter = new FileWriter(path.toFile(), true);
                        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                        bufferedWriter.write("  {");
                        bufferedWriter.write("    \"id\": " + id + ",");
                        bufferedWriter.write("    \"radius\": \"" + Config.NPC_WALKING_RADIUS + "\",");
                        bufferedWriter.write("    \"facing\": \"NORTH\",");
                        bufferedWriter.write("    \"position\": {");
                        bufferedWriter.write("      \"x\": " + player.getPosition().getX() + ",");
                        bufferedWriter.write("      \"y\": " + player.getPosition().getY() + ",");
                        bufferedWriter.write("      \"height\": " + player.getPosition().getHeight() + "");
                        bufferedWriter.write("    }");
                        bufferedWriter.write("  },");
                        bufferedWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    player.send(new SendMessage("Npc " + id + " has been spawned."));
                }
            }
        });

        commands.add(new Command("points") {
            @Override
            public void execute(Player player, CommandParser parser) {
                player.dragonfireCharges = 50;
                player.slayer.setPoints(50000);
                player.donation.setCredits(50000);
                player.votePoints = 500000;
                player.pestPoints = 500000;
                player.skillingPoints = 500000;
                player.message("Enjoy deh points");
            }
        });

        commands.add(new Command("demote") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    StringBuilder name = new StringBuilder(parser.nextString());
                    while (parser.hasNext()) {
                        name.append(" ").append(parser.nextString());
                    }
                    World.search(name.toString()).ifPresent(other -> {
                        if (PlayerRight.isDeveloper(other)) {
                            return;
                        }

                        other.right = PlayerRight.PLAYER;
                        other.dialogueFactory.sendStatement("You have been demoted!").execute();
                        player.message("demote was complete");
                    });
                } else {
                    player.message("Invalid command use; ::demote settings");
                }
            }
        });

        commands.add(new Command("save", "saveworld", "savegame") {
            @Override
            public void execute(Player player, CommandParser parser) {
                World.save();
                player.send(new SendMessage("All data has been successfully saved."));
            }
        });

        commands.add(new Command("bank") {
            @Override
            public void execute(Player player, CommandParser parser) {
                player.bank.open();
            }
        });

        commands.add(new Command("move") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext(3)) {
                    int x = parser.nextInt();
                    int y = parser.nextInt();
                    int z = parser.nextInt();
                    player.move(player.getPosition().transform(x, y, z));
                } else if (parser.hasNext(2)) {
                    int x = parser.nextInt();
                    int y = parser.nextInt();
                    int z = player.getHeight();
                    player.move(player.getPosition().transform(x, y, z));
                } else return;

                if (player.debug) {
                    player.send(new SendMessage("You have teleported to the coordinates: " + player.getPosition(), MessageColor.BLUE));
                }
            }
        });

        commands.add(new Command("tele") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext(3)) {
                    int x = parser.nextInt();
                    int y = parser.nextInt();
                    int z = parser.nextInt();
                    player.move(new Position(x, y, z));
                } else if (parser.hasNext(2)) {
                    int x = parser.nextInt();
                    int y = parser.nextInt();
                    int z = player.getHeight();
                    player.move(new Position(x, y, z));
                } else return;
                if (player.debug) {
                    player.send(new SendMessage("You have teleported to the coordinates: " + player.getPosition(), MessageColor.BLUE));
                }
            }
        });

        commands.add(new Command("spellbook") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    String spellbook = parser.nextString();
                    switch (spellbook.toUpperCase()) {
                        case "LUNAR":
                            player.spellbook = Spellbook.LUNAR;
                            break;
                        case "MODERN":
                            player.spellbook = Spellbook.MODERN;
                            break;
                        case "ANCIENT":
                            player.spellbook = Spellbook.ANCIENT;
                            break;
                    }
                    player.interfaceManager.setSidebar(Config.MAGIC_TAB, player.spellbook.getInterfaceId());
                }
            }
        });

        commands.add(new Command("starterbank") {
            @Override
            public void execute(Player player, CommandParser parser) {
                player.bank.clear();
                player.bank.addAll(Config.STARTER_BANK);
                System.arraycopy(Config.STARTER_BANK_AMOUNT, 0, player.bank.tabAmounts, 0, Config.STARTER_BANK_AMOUNT.length);
                player.bank.shift();
                player.bank.open();
            }
        });

        commands.add(new Command("bigbank") {
            @Override
            public void execute(Player player, CommandParser parser) {
                player.bank.clear();
                player.bank.addAll(Config.LEET_BANK_ITEMS);
                System.arraycopy(Config.LEET_BANK_AMOUNTS, 0, player.bank.tabAmounts, 0, Config.LEET_BANK_AMOUNTS.length);
                player.bank.shift();
                player.bank.open();
            }
        });

        commands.add(new Command("item", "pickup") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    int id = parser.nextInt();
                    int amount = 1;
                    if (parser.hasNext()) {
                        amount = Integer.parseInt(parser.nextString().toLowerCase().replace("k", "000").replace("m", "000000").replace("b", "000000000"));
                    }

                    final ItemDefinition def = ItemDefinition.get(id);

                    if (def == null || def.getName() == null) {
                        return;
                    }

                    if (def.getName().equalsIgnoreCase("null")) {
                        return;
                    }

                    player.inventory.add(id, amount);
                }
            }
        });

        commands.add(new Command("find", "give") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    final String name = parser.nextLine();
                    ItemContainer container = new ItemContainer(400, ItemContainer.StackPolicy.ALWAYS);
                    int count = 0;
                    for (final ItemDefinition def : ItemDefinition.DEFINITIONS) {
                        if (def == null || def.getName() == null || def.isNoted())
                            continue;
                        if (def.getName().toLowerCase().trim().contains(name)) {
                            container.add(new Item(def.getId()));
                            count++;
                            if (count == 400)
                                break;
                        }
                    }
                    player.send(new SendString("Search: <col=FF5500>" + name, 37506));
                    player.send(new SendString(String.format("Found <col=FF5500>%s</col> item%s", count, count != 1 ? "s" : ""), 37507));
                    player.send(new SendScrollbar(37520, count / 8 * 52 + ((count % 8) == 0 ? 0 : 52)));
                    player.send(new SendItemOnInterface(37521, container.getItems()));
                    player.interfaceManager.open(37500);
                    player.send(new SendMessage(String.format("Found %s item%s containing the key '%s'.", count, count != 1 ? "s" : "", name)));
                }
            }
        });

        commands.add(new Command("pos", "mypos", "coords") {
            @Override
            public void execute(Player player, CommandParser parser) {
                player.send(new SendMessage("Your location is: " + player.getPosition() + "."));
                System.out.println("Your location is: " + player.getPosition() + ".");
            }
        });
    }

    @Override
    public boolean canAccess(Player player) {
        return PlayerRight.isAdministrator(player);
    }
}
