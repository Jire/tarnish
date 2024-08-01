package plugin.command;

import com.osroyale.Config;
import com.osroyale.content.activity.inferno.Inferno;
import com.osroyale.content.bot.PlayerBot;
import com.osroyale.content.bot.objective.BotObjective;
import com.osroyale.content.clanchannel.ClanRepository;
import com.osroyale.content.clanchannel.channel.ClanChannel;
import com.osroyale.content.skill.SkillRepository;
import com.osroyale.content.store.Store;
import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.plugin.extension.CommandExtension;
import com.osroyale.game.task.impl.ObjectPlacementEvent;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.attack.listener.CombatListenerManager;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.hit.Hitsplat;
import com.osroyale.game.world.entity.combat.strategy.CombatStrategy;
import com.osroyale.game.world.entity.combat.strategy.player.special.CombatSpecial;
import com.osroyale.game.world.entity.mob.Direction;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.npc.definition.NpcDefinition;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.entity.mob.player.command.Command;
import com.osroyale.game.world.entity.mob.player.command.CommandParser;
import com.osroyale.game.world.entity.mob.player.profile.ProfileRepository;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.ItemDefinition;
import com.osroyale.game.world.object.CustomGameObject;
import com.osroyale.game.world.object.GameObjectDefinition;
import com.osroyale.game.world.object.ObjectDirection;
import com.osroyale.game.world.object.ObjectType;
import com.osroyale.game.world.pathfinding.TraversalMap;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.out.SendAnnouncement;
import com.osroyale.net.packet.out.SendConfig;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.util.MessageColor;
import com.osroyale.util.RandomUtils;
import com.osroyale.util.Utility;
import com.osroyale.util.parser.impl.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.osroyale.game.world.entity.combat.attack.FormulaFactory.*;

public class DeveloperCommandPlugin extends CommandExtension {

    @Override
    protected void register() {
        commands.add(new Command("test") {
            @Override
            public void execute(Player player, CommandParser parser) {
            }
        });
        commands.add(new Command("master") {
            @Override
            public void execute(Player player, CommandParser parser) {
                player.skills.master();
            }
        });
        commands.add(new Command("fpatch") {
            @Override
            public void execute(Player player, CommandParser parser) {
                int index = parser.nextInt();
                int state = parser.nextInt();
                int growth = parser.nextInt();
                int config = (state << 6) | growth;
                config = config << (index << 3);
                player.send(new SendConfig(529, config));
            }
        });
        commands.add(new Command("inferno") {
            @Override
                    public void execute(Player player, CommandParser parser) {
                Inferno.create(player);
                player.send(new SendMessage("Inferno created."));
                }
            });

        commands.add(new Command("rngbank") {
            @Override
            public void execute(Player player, CommandParser parser) {
                player.bank.clear();
                while (player.bank.size() < player.bank.capacity()) {
                    Item item = new Item(RandomUtils.inclusive(1, 20_000));
                    if (!item.getName().contains("null"))
                        player.bank.depositFromNothing(item.unnoted(), RandomUtils.inclusive(0, 9));
                }

                System.out.println(Arrays.toString(player.bank.tabAmounts));
            }
        });

        commands.add(new Command("reloadclans") {
            @Override
            public void execute(Player player, CommandParser parser) {
                Path path = Paths.get("./data/content/clan/");
                File[] files = path.toFile().listFiles();

                if (files == null) {
                    player.message("No clan files were found.");
                    return;
                }

                int failed = 0;
                for (File file : files) {
                    String owner = file.getName().replaceAll(".json", "").toLowerCase().trim();
                    if (ClanRepository.getChannel(owner) == null) {
                        player.message("<col=FF0000>Reloading " + file.getName());
                        ClanChannel.load(owner);
                        failed++;
                    }
                }
                player.message("<col=FF0000>Reloaded " + failed + "</col> clans.");
            }
        });

        commands.add(new Command("clansloaded") {
            @Override
            public void execute(Player player, CommandParser parser) {
                Path path = Paths.get("./data/content/clan/");
                File[] files = path.toFile().listFiles();

                if (files == null) {
                    player.message("No clan files were found.");
                    return;
                }

                int loaded = 0;
                int failed = 0;
                int total = 0;

                for (File file : files) {
                    if (ClanRepository.getChannel(file.getName().replaceAll(".json", "").toLowerCase().trim()) != null) {
                        loaded++;
                    } else {
                        player.message("<col=FF0000>Failed to load " + file.getName());
                        failed++;
                    }
                    total++;
                }

                player.message("Loaded <col=FF0000>" + loaded + "</col> out of <col=FF0000>" + total + "</col>, failed to load <col=FF0000>" + failed);
            }
        });

        commands.add(new Command("leet", "l33t") {
            @Override
            public void execute(Player player, CommandParser parser) {
                for (int index = 0; index < 6; index++) {
                    player.skills.setLevel(index, 99999);
                }
            }
        });
        commands.add(new Command("healthh") {
            @Override
            public void execute(Player player, CommandParser parser) {
                player.skills.setLevel(3, Integer.MAX_VALUE);
                player.skills.setLevel(5, Integer.MAX_VALUE);

            }
        });
        commands.add(new Command("up") {
            @Override
            public void execute(Player player, CommandParser parser) {
                player.move(player.getPosition().transform(0, 0, 1));
            }
        });

        commands.add(new Command("down") {
            @Override
            public void execute(Player player, CommandParser parser) {
                player.move(player.getPosition().transform(0, 0, -1));
            }
        });

        commands.add(new Command("skill", "lvl", "level") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext(2)) {
                    player.skills.setMaxLevel(parser.nextInt(), parser.nextInt());
                }
            }
        });

        commands.add(new Command("myregion") {
            @Override
            public void execute(Player player, CommandParser parser) {
                player.send(new SendMessage(String.format("region=%d", player.getRegion().getId())));
            }
        });

        commands.add(new Command("botcount", "botsize") {
            @Override
            public void execute(Player player, CommandParser parser) {
                int size = World.getBotCount();
                player.send(new SendAnnouncement("There are currently:", size + " bots online mother fucker!", 0X770077));
            }
        });

        commands.add(new Command("clearbots", "clearbot") {
            @Override
            public void execute(Player player, CommandParser parser) {
                World.getPlayers().forEach(it -> {
                    if (it.isBot) {
                        it.unregister();
                    }
                });
            }
        });

        commands.add(new Command("bot") {
            @Override
            public void execute(Player player, CommandParser parser) {
                int count = parser.nextInt();
                for (int i = 1; i <= count; i++) {
                    PlayerBot bot = new PlayerBot("New Bot " + i);
                    bot.register();
                    BotObjective.WALK_TO_BANK.init(bot);
                }
            }
        });


        commands.add(new Command("spec") {
            @Override
            public void execute(Player player, CommandParser parser) {
                int amount = 100000;
                if (parser.hasNext()) {
                    amount = parser.nextInt();
                }
                CombatSpecial.restore(player, amount);
            }
        });

        commands.add(new Command("copyme") {
            @Override
            public void execute(Player player, CommandParser parser) {
                final String name = parser.nextLine();
                Optional<Player> other = World.search(name);
                other.ifPresent(player.playerAssistant::copy);
            }
        });

        commands.add(new Command("health") {
            @Override
            public void execute(Player player, CommandParser parser) {
                player.skills.modifyLevel(hp -> 10_000, Skill.HITPOINTS, 0, 10_000);
                player.skills.modifyLevel(hp -> 10_000, Skill.PRAYER, 0, 10_000);
                player.skills.refresh(Skill.HITPOINTS);
                player.skills.refresh(Skill.PRAYER);
            }
        });

        commands.add(new Command("lnpcs") {
            @Override
            public void execute(Player player, CommandParser parser) {
                int size = parser.nextInt();
                int diameter = size * size;
                for (int index = 0; index < diameter; index++) {
                    Position position = player.getPosition().transform(index % size, index / size, 0);
                    Npc man = new Npc(3080, position);
                    man.skills.setLevel(3, 450);
                    man.register();
                }
//                for (Npc npc : spawned) {
//                    if (npc == middle) continue;
//                    if (npc.getCombat().inCombat()) break;
//                    npc.getCombat().attack(middle);
//                }
            }
        });


        commands.add(new Command("snpc", "searchnpc") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    final String name = parser.nextLine();

                    for (int i = 0; i < NpcDefinition.DEFINITIONS.length; i++) {
                        NpcDefinition def = NpcDefinition.get(i);
                        if (def == null) {
                            continue;
                        }

                        String npcName = def.getName();

                        if (npcName == null) {
                            continue;
                        }

                        if (npcName.contains(name)) {
                            player.send(new SendMessage(String.format("%s=%d", npcName, i)));
                        }

                    }
                }
            }
        });

        commands.add(new Command("update") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    int time = parser.nextInt() * 100;
                    World.update(time);
                }
            }
        });

        commands.add(new Command("debug") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    final String type = parser.nextString().toLowerCase();

                    switch (type) {

                        case "s":
                        case "server":
                            Config.SERVER_DEBUG = !Config.SERVER_DEBUG;
                            player.send(new SendMessage(String.format("server debug=%s", Config.SERVER_DEBUG ? "on" : "off"), MessageColor.DEVELOPER));
                            break;

                        case "p":
                        case "player":
                            player.debug = !player.debug;
                            player.send(new SendMessage(String.format("player debug=%s", player.debug ? "on" : "off"), MessageColor.DEVELOPER));
                            break;
                    }
                }
            }
        });

        commands.add(new Command("dumpequip") {
            @Override
            public void execute(Player player, CommandParser parser) {
                System.out.print("");
                for (final Item item : player.equipment.toArray()) {
                    if (item == null) {
                        continue;
                    }
                    if (item.getAmount() > 1) {
                        System.out.printf("new Item(%s, %s), ", item.getId(), item.getAmount());
                    } else {
                        System.out.printf("new Item(%s), ", item.getId());
                    }
                }
                System.out.print("");
                System.out.println();
            }
        });

        commands.add(new Command("dumpinv") {
            @Override
            public void execute(Player player, CommandParser parser) {
                boolean justId = parser.hasNext();

                System.out.print("{ ");
                for (final Item item : player.inventory) {
                    if (item == null) {
                        continue;
                    }

                    if (justId) {
                        System.out.print(item.getId() + ", ");
                    } else {
                        if (item.getAmount() > 1) {
                            System.out.println("new Item(" + item.getId() + ", " + item.getAmount() + "), // " + item.getName().toUpperCase());
                        } else {
                            System.out.println("new Item(" + item.getId() + "), // " + item.getName().toUpperCase());
                        }
                    }
                }

                System.out.print(" }");
                System.out.println();
            }
        });

        commands.add(new Command("dumpinv2") {
            @Override
            public void execute(Player player, CommandParser parser) {
                for (final Item item : player.inventory.toArray()) {
                    if (item != null) {
                        int price = item.getSellValue();
                        double bonus = 11;
                        System.out.println("         {");
                        System.out.println("            \"id\":" + item.getId() + ",");
                        System.out.println("            \"amount\":100");
                        System.out.println("         },");
                    }
//                    System.out.println();
                }

            }
        });

        commands.add(new Command("die") {
            @Override
            public void execute(Player player, CommandParser parser) {
                Hit hit = new Hit(player.getCurrentHealth(), Hitsplat.NORMAL);
                player.getCombat().getDamageCache().add(player, hit);
                player.damage(hit);
            }
        });

        commands.add(new Command("face") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    Direction direction = Direction.valueOf(parser.nextString().toUpperCase());
                    player.resetFace();
                    player.face(direction);
                    player.send(new SendMessage("You are now facing direction: " + direction.name().toLowerCase() + "."));
                }
            }
        });
        commands.add(new Command("checkaccs") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (PlayerRight.isModerator(player) && player.managing.isPresent()) {
                    Player other = player.managing.get();
                    List<String> list = ProfileRepository.getRegistry(other.lastHost);

                    if (!list.isEmpty()) {
                        for (int index = 0; index < 50; index++) {
                            String name = index >= list.size() ? "" : list.get(index);
                            player.send(new SendString(name, 37111 + index));
                        }

                        player.message("<col=FF0D5D>There are " + list.size() + " accounts linked to " + Utility.formatName(other.getName()) + ".");
                        player.send(new SendString("Profiles:\\n" + list.size(), 37107));
                        player.send(new SendString(other.getName(), 37103));
                        player.interfaceManager.open(37100);
                    }
                }
            }
        });

        commands.add(new Command("store", "shop") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    final String name = parser.nextLine();
                    Store.STORES.get(name).open(player);
                }
            }
        });

        commands.add(new Command("region") {
            @Override
            public void execute(Player player, CommandParser parser) {
                Set<Direction> directions = EnumSet.noneOf(Direction.class);
                boolean projectiles = false;
                boolean exact = false;

                while (parser.hasNext()) {
                    switch (parser.nextString().toLowerCase()) {
                        case "n":
                            directions.add(Direction.NORTH);
                            break;
                        case "e":
                            directions.add(Direction.EAST);
                            break;
                        case "s":
                            directions.add(Direction.SOUTH);
                            break;
                        case "w":
                            directions.add(Direction.WEST);
                            break;
                        case "ne":
                            directions.add(Direction.NORTH_EAST);
                            break;
                        case "nw":
                            directions.add(Direction.NORTH_WEST);
                            break;
                        case "se":
                            directions.add(Direction.SOUTH_EAST);
                            break;
                        case "sw":
                            directions.add(Direction.SOUTH_WEST);
                            break;
                        case "all":
                            directions.addAll(Arrays.asList(Direction.values()));
                            break;
                        case "p":
                            projectiles = true;
                            break;
                        case "ex":
                            exact = true;
                            break;
                    }
                }

                for (int y = -20; y < 20; y++) {
                    for (int x = -40; x < 40; x++) {
                        Position position = new Position(player.getX() + x, player.getY() - y, player.getHeight());
                        boolean clear = !exact;
                        for (Direction direction : directions) {
                            if (exact) {
                                if (TraversalMap.isTraversable(position, direction, projectiles)) {
                                    clear = true;
                                }
                            } else {
                                if (!TraversalMap.isTraversable(position, direction, projectiles)) {
                                    clear = false;
                                }
                            }
                        }

                        if (x == 0 && y == 0) {
                            System.out.print(" +");
                        } else if (!clear) {
                            System.out.print(" *");
                        } else {
                            System.out.print("  ");
                        }
                    }
                    System.out.println();
                }
                System.out.println();
            }
        });

        commands.add(new Command("clip") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext(2)) {
                    int dx = parser.nextInt();
                    int dy = parser.nextInt();

                    if (parser.hasNext())
                        player.movement.dijkstraPath(player.getPosition().transform(dx, dy));
                    else
                        player.movement.simplePath(player.getPosition().transform(dx, dy));
                }
            }
        });

        commands.add(new Command("ro", "regionobjects") {
            @Override
            public void execute(Player player, CommandParser parser) {
                for (int y = -20; y < 20; y++) {
                    for (int x = -40; x < 40; x++) {
                        Position position = new Position(player.getX() + x, player.getY() - y, player.getHeight());

                        if (x == 0 && y == 0) {
                            System.out.print(" +");
                        } else if (position.getRegion().containsObject(position)) {
                            System.out.print(" *");
                        } else {
                            System.out.print("  ");
                        }
                    }
                    System.out.println();
                }
            }
        });

        commands.add(new Command("obj", "object") {
            @Override
            public void execute(Player player, CommandParser parser) {
                int id = parser.nextInt();
                int rotation = 0;
                if (parser.hasNext()) {
                    rotation = parser.nextInt();
                }
                CustomGameObject gameObject = new CustomGameObject(id, player.getPosition().copy(), ObjectDirection.valueOf(rotation).orElse(ObjectDirection.WEST), ObjectType.GENERAL_PROP);
                World.schedule(new ObjectPlacementEvent(gameObject, 50));
                player.send(new SendMessage("Spawned temporary object " + id + "."));
            }
        });

        commands.add(new Command("dumpbank") {
            @Override
            public void execute(Player player, CommandParser parser) {
                int[] amounts = player.bank.tabAmounts;
                Item[] items = player.bank.toArray();
                String data = " ";
                for (int am : amounts) {
                    data += am + ", ";
                }

                System.out.println("\tpublic final static int[] tabAmounts = {" + data + "};");
                System.out.println("");
                data = "\n\t\t";
                int c = 0;
                int iamounts = 0;
                for (Item it : items) {
                    if (it == null) continue;
                    iamounts = it.isStackable() ? 100000 : 1000;
                    data += "new Item(" + it.getId() + ", " + iamounts + "), ";
                    if (++c % 5 == 0) {
                        data += "\n\t\t";
                    }
                }

                data += "\n\t";
                System.out.println("\tprivate Item[] bankItems = {" + data + "};");
            }
        });

        commands.add(new Command("hit") {
            @Override
            public void execute(Player player, CommandParser parser) {
                player.damage(new Hit(parser.nextInt()));
            }
        });

        commands.add(new Command("setinst", "setinstance") {
            @Override
            public void execute(Player player, CommandParser parser) {
                player.instance = parser.nextByte();
                player.send(new SendMessage(player.instance));
            }
        });

        commands.add(new Command("gfx", "graphic") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    int id = parser.nextInt();
                    boolean high = parser.hasNext();
                    player.graphic(new Graphic(id, high));
                    player.send(new SendMessage("Performing graphic = " + id));
                }

            }
        });

        commands.add(new Command("int", "interface") {
            @Override
            public void execute(Player player, CommandParser parser) {
                int id = parser.nextInt();
                player.interfaceManager.open(id);
                player.send(new SendMessage("Opening interface: " + id, MessageColor.LIGHT_PURPLE));
            }
        });

        commands.add(new Command("anim") {
            @Override
            public void execute(Player player, CommandParser parser) {
                int id = parser.nextInt();
                player.animate(new Animation(id));
                player.send(new SendMessage("Performing animation = " + id));

            }
        });

        commands.add(new Command("tpost") {
            @Override
            public void execute(Player player, CommandParser parser) {
                player.tradingPost.openOverviewInterface();

            }
        });

        commands.add(new Command("npc") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    int id = parser.nextInt();
                    Npc npc = new Npc(id, player.getPosition());
                    npc.walkingRadius = Config.NPC_WALKING_RADIUS;
                    npc.walk = false;
                    npc.register();

                    if (id == 3080) {
                        npc.skills.setNpcMaxLevel(3, 99_999);
                        npc.locking.lock();
                    }
                    if (id == 2267 || id == 2266 || id == 2265) {
                        npc.locking.lock();
                    }
                    if (id == 2075) {
                        npc.skills.setNpcMaxLevel(3, 800);
                    }
                    player.send(new SendMessage("Npc " + id + " has been spawned."));
                }
            }
        });

        commands.add(new Command("sobj", "searchobj") {
            @Override
            public void execute(Player player, CommandParser parser) {
                final String search = parser.nextLine();
                player.send(new SendMessage("Searching fs for object name: " + search, MessageColor.DEVELOPER));
                int count = 0;
                for (GameObjectDefinition def : GameObjectDefinition.definitions) {
                    if (def == null) continue;
                    if (def.getName().contains(search.toString())) {
                        player.send(new SendMessage(def.getId() + ": " + def.getName(), MessageColor.DEVELOPER));
                        count++;
                    }
                }
                player.send(new SendMessage(count + " results were found for the query: " + search, MessageColor.DEVELOPER));
            }
        });

        commands.add(new Command("reint") {
            @Override
            public void execute(Player player, CommandParser parser) {
                player.playerAssistant.clearSendStrings();
            }
        });

        commands.add(new Command("hide", "invis") {
            @Override
            public void execute(Player player, CommandParser parser) {
                player.setVisible(!player.isVisible());
                player.send(new SendMessage(String.format("You are now %s.", player.isVisible() ? "visible" : "hidden")));
            }
        });

        commands.add(new Command("getnpcroll") {
            @Override
            public void execute(Player player, CommandParser parser) {
                Npc npc = new Npc(parser.nextInt(), player.getPosition());
                NpcDefinition definition = NpcDefinition.get(npc.id);
                npc.definition = definition;
                npc.pathfinderProjectiles = Mob.pathfinderProjectiles(npc);
                npc.setBonuses(definition.getBonuses());

                for (int index = 0; index < definition.getSkills().length; index++) {
                    npc.skills.setNpcMaxLevel(index, definition.getSkills()[index]);
                }

                CombatStrategy<? super Player> strategy = player.getStrategy();
                CombatType type = strategy.getCombatType();

                player.getCombat().addModifier(strategy);

                int max = getModifiedMaxHit(player, npc, type);
                double attackRoll = rollOffensive(player, npc, type.getFormula());
                double defenceRoll = rollDefensive(player, npc, type.getFormula());
                double chance = attackRoll / (attackRoll + defenceRoll);
                double accuracy = (int) (chance * 10000) / 100.0;

                player.message("");
                player.message("You have <col=FF0000>" + accuracy + "%</col> accuracy against " + npc.getName() + ".");
                player.message("Your max hit against " + npc.getName() + " is <col=FF0000>" + max);
                player.message("Attack roll: <col=FF0000>" + (int) attackRoll + "</col>  ---  Defence roll: <col=FF0000>" + (int) defenceRoll);

                String rolls = "";
                rolls += "accuracy: <col=FF0000>" + player.getCombat().modifyAccuracy(npc, 100) + "%</col>  ---  ";
                rolls += "defence: <col=FF0000>" + player.getCombat().modifyDefensive(npc, 100) + "%</col>  --- ";
                rolls += "damage: <col=FF0000>" + player.getCombat().modifyDamage(npc, 100) + "%";

                String levels = "";
                levels += "attack: <col=FF0000>" + player.getCombat().modifyAttackLevel(npc, 100) + "%</col> -- ";
                levels += "strength: <col=FF0000>" + player.getCombat().modifyStrengthLevel(npc, 100) + "%</col> -- ";
                levels += "defence: <col=FF0000>" + player.getCombat().modifyDefenceLevel(npc, 100) + "%</col> -- ";
                levels += "ranged: <col=FF0000>" + player.getCombat().modifyRangedLevel(npc, 100) + "%</col> -- ";
                levels += "magic: <col=FF0000>" + player.getCombat().modifyMagicLevel(npc, 100) + "%";

                player.getCombat().removeModifier(strategy);
                player.message(rolls, levels);
            }
        });

        commands.add(new Command("getroll") {
            @Override
            public void execute(Player player, CommandParser parser) {
                StringBuilder name = new StringBuilder(parser.nextString());
                while (parser.hasNext()) name.append(" ").append(parser.nextString());
                Optional<Player> search = World.search(name.toString().trim());

                search.ifPresent(other -> {
                    CombatStrategy<? super Player> strategy = player.getStrategy();

                    CombatType type = strategy.getCombatType();
                    player.getCombat().addModifier(strategy);

                    int max = getModifiedMaxHit(player, other, type);
                    double attackRoll = rollOffensive(player, other, type.getFormula());
                    double defenceRoll = rollDefensive(player, other, type.getFormula());
                    double chance = attackRoll / (attackRoll + defenceRoll);
                    double accuracy = (int) (chance * 10000) / 100.0;

                    player.send(new SendMessage(""));
                    player.send(new SendMessage("You have <col=FF0000>" + accuracy + "%</col> accuracy against " + other.getName() + "."));
                    player.send(new SendMessage("Your max hit against " + other.getName() + " is <col=FF0000>" + max));
                    player.send(new SendMessage("Attack roll: <col=FF0000>" + (int) attackRoll + "</col>  ---  Defence roll: <col=FF0000>" + (int) defenceRoll));

                    String rolls = "";
                    rolls += "accuracy: <col=FF0000>" + player.getCombat().modifyAccuracy(other, 100) + "%</col>  ---  ";
                    rolls += "defencive: <col=FF0000>" + player.getCombat().modifyDefensive(other, 100) + "%</col>  --- ";
                    rolls += "damage: <col=FF0000>" + player.getCombat().modifyDamage(other, 100) + "%";

                    String levels = "";
                    levels += "attack: <col=FF0000>" + player.getCombat().modifyAttackLevel(other, 100) + "%</col> -- ";
                    levels += "strength: <col=FF0000>" + player.getCombat().modifyStrengthLevel(other, 100) + "%</col> -- ";
                    levels += "defence: <col=FF0000>" + player.getCombat().modifyDefenceLevel(other, 100) + "%</col> -- ";
                    levels += "ranged: <col=FF0000>" + player.getCombat().modifyRangedLevel(other, 100) + "%</col> -- ";
                    levels += "magic: <col=FF0000>" + player.getCombat().modifyMagicLevel(other, 100) + "%";

                    player.getCombat().removeModifier(strategy);
                    player.message(rolls, levels);
                });
            }
        });

        commands.add(new Command("reload") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (!parser.hasNext()) {
                    player.send(new SendMessage("Invalid use of the reload command. Usage: ::reload object"));
                    return;
                }

                while (parser.hasNext()) {
                    switch (parser.nextString().toUpperCase()) {
                        case "LISTENER":
                            CombatListenerManager.load();
                            player.send(new SendMessage("Combat listeners have been successfully loaded."));
                            break;
                        case "PROJECTILE":
                        case "PROJ":
                            new CombatProjectileParser().run();
                            player.send(new SendMessage("Projectiles have been successfully loaded."));
                            break;
                        case "ITEM":
                            ItemDefinition.createParser().run();
                            player.send(new SendMessage("Items have been successfully loaded."));
                            break;
                        case "OBJECT":
                        case "OBJ":
                            new GlobalObjectParser().run();
                            player.send(new SendMessage("Objects have been successfully loaded."));
                            break;
                        case "COMBAT":
                            new CombatProjectileParser().run();
                            player.send(new SendMessage("Combat projectiles have been successfully loaded."));
                            World.getNpcs().forEach(Npc::unregister);
                            NpcDefinition.createParser().run();
                            new NpcSpawnParser().run();
                            new NpcForceChatParser().run();
                            player.send(new SendMessage("Npc spawns have been successfully loaded."));
                            ItemDefinition.createParser().run();
                            player.send(new SendMessage("Items have been successfully loaded."));
                            CombatListenerManager.load();
                            player.send(new SendMessage("Combat listeners have been successfully loaded."));
                            break;
                        case "NPC":
                        case "SPAWN":
                            for (Npc npc : World.getNpcs()) {
                                npc.unregister();
                            }

                            World.schedule(3, () -> {
                                NpcDefinition.createParser().run();
                                new NpcSpawnParser().run();
                                new NpcForceChatParser().run();
                                new NpcDropParser().run();
                                player.send(new SendMessage("Npc spawns have been successfully loaded."));
                            });
                            break;
                        case "SKILL":
                        case "SKILLS":
                            SkillRepository.load();
                            player.send(new SendMessage("Skills have been successfully loaded."));
                            break;
                        case "STORE":
                        case "SHOP":
                            Store.STORES.clear();
                            new StoreParser().run();
                            player.send(new SendMessage("Stores have been successfully loaded."));
                            break;

                        default:
                            player.send(new SendMessage("No reload entry was found."));
                            break;
                    }
                }

            }
        });

    }

    @Override
    public boolean canAccess(Player player) {
        return PlayerRight.isDeveloper(player);
    }

}
