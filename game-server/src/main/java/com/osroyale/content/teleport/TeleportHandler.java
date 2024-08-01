package com.osroyale.content.teleport;

import com.osroyale.content.activity.impl.barrows.Barrows;
import com.osroyale.content.activity.impl.godwars.GodwarsActivity;
import com.osroyale.content.activity.impl.warriorguild.WarriorGuild;
import com.osroyale.content.activity.inferno.Inferno;
import com.osroyale.content.dialogue.DialogueFactory;
import com.osroyale.content.skill.impl.magic.teleport.TeleportType;
import com.osroyale.content.skill.impl.magic.teleport.Teleportation;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.out.*;
import com.osroyale.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles teleporting to various locations around OS Royale.
 *
 * @author Daniel
 */
public class TeleportHandler {

    /** Holds the teleport titles names. */
    private static final String[] TITLES = {"Favorites", "Minigames", "Skilling", "Monster Killing", "Player Killing", "Boss Killing"};

    /** Opens the teleport itemcontainer. */
    public static void open(Player player) {
        open(player, TeleportType.FAVORITES, 0);
    }

    /** Opens the teleport itemcontainer. */
    public static void open(Player player, TeleportType type) {
        open(player, type, 0);
    }

    /** Opens the teleport itemcontainer to a certain teleportNoChecks type. */
    public static void open(Player player, TeleportType type, int teleportIndex) {
        List<Teleport> teleports = type == TeleportType.FAVORITES ? player.favoriteTeleport : getTeleports(type);
        player.attributes.set("TELEPORT_TYPE_KEY", type);
        if (player.attributes.get("TELEPORT_INDEX_KEY", Integer.class) == null)
            player.attributes.set("TELEPORT_INDEX_KEY", 0);
        int size = teleports.size();
        for (int index = 0, string = 58009; index < 6; index++) {
            String color = "<col=" + (type.ordinal() == index ? "ffffff" : "ff9933") + ">";
            player.send(new SendString(color + TITLES[index], string));
            string += 4;
        }
        for (int index = 0, string = 58052; index < (size < 9 ? 9 : size); index++, string += 2) {
            if (index >= size) {
                player.send(new SendString("", string));
                continue;
            }
            Teleport teleport = teleports.get(index);
            String prefex = teleportIndex == index ? "<col=ffffff>" : "</col>";
            String favorite = player.favoriteTeleport.contains(teleport) ? "<clan=6> " : "";
            player.send(new SendString(favorite + prefex + teleport.getName(), string));
        }
        if (type == TeleportType.FAVORITES && player.favoriteTeleport.isEmpty()) {
            display(player, null);
        } else {
            display(player, teleports.get(teleportIndex));
        }
        player.send(new SendScrollbar(58050, size <= 9 ? 225 : (size * 25)));
        player.interfaceManager.open(58000);
    }

    /** Displays all the teleport text on the itemcontainer. */
    public static void display(Player player, Teleport teleport) {
        player.attributes.set("TELEPORT", teleport);
        player.send(new SendConfig(348, player.favoriteTeleport.contains(teleport) ? 0 : 1));

        if (teleport != null) {
            Item items[] = new Item[3];
            for (int index = 0, count = 0; index < teleport.getDisplay().length; index++, count++) {
                if (teleport.getDisplay()[index] == -1) {
                    items[count] = null;
                    continue;
                }
                Item item = new Item(teleport.getDisplay()[index]);
                if (item.isStackable())
                    item.setAmount(50000);
                items[count] = item;
            }
            player.send(new SendItemOnInterface(58041, items));
        }

        if (teleport != null && teleport.getStrings()[0].length() == 0 && teleport.getStrings()[1].length() == 0) {
            player.send(new SendString(teleport.getName(), 58031));
            player.send(new SendString("", 58032));
            player.send(new SendString("", 58033));
            return;
        } else if (teleport == null) {
            player.send(new SendString("", 58031));
            player.send(new SendString("You do not have any teleport selected", 58032));
            player.send(new SendString("", 58033));
            player.send(new SendItemOnInterface(58041));
            return;
        }

        player.send(new SendString(teleport.getName(), 58031));
        player.send(new SendString("<col=ff7000>" + teleport.getStrings()[0], 58032));
        player.send(new SendString("<col=ff7000>" + teleport.getStrings()[1], 58033));
    }

    /** Handles clicking teleport buttons on the itemcontainer. */
    public static void click(Player player, int button) {
        TeleportType type = player.attributes.get("TELEPORT_TYPE_KEY", TeleportType.class);
        List<Teleport> teleports = type == TeleportType.FAVORITES ? player.favoriteTeleport : getTeleports(type);
        int index = getOrdinal(button);
        player.attributes.set("TELEPORT_INDEX_KEY", index);
        open(player, type, index);
        if (index < teleports.size()) {
            display(player, teleports.get(index));
        }
    }

    /** Handles teleporting to the destination. */
    public static void teleport(Player player) {
        if (player.wilderness > 20 && !PlayerRight.isAdministrator(player)) {
            player.send(new SendMessage("You can't teleport past 20 wilderness!"));
            return;
        }


        Teleport teleport = player.attributes.get("TELEPORT", Teleport.class);
        if (teleport == null) {
            player.send(new SendMessage("You have not selected a destination to teleport to!"));
            return;
        }
        player.lastTeleport = teleport;
        if (teleport.getName() == "Inferno") {
            Inferno.create(player);
            player.message("Welcome To Inferno " + player.getUsername());
        }
        if (teleport.isSpecial()) {
            special(player, teleport);
            return;
        }
        Teleportation.teleport(player, teleport.getPosition());
        player.send(new SendMessage("You have teleported to " + teleport.getName() + ".", true));
    }

    /** Handles favorite a teleport. */
    public static void favorite(Player player) {
        Teleport teleport = player.attributes.get("TELEPORT", Teleport.class);
        if (teleport == null) {
            player.send(new SendMessage("You have not selected a teleport to favorite!"));
            player.send(new SendConfig(348, 1));
            return;
        }
        boolean isFavorite = player.favoriteTeleport.contains(teleport);
        int index = player.attributes.get("TELEPORT_INDEX_KEY", Integer.class);
        if (index == -1) {
            index = 0;
        }

        if (isFavorite) {
            player.favoriteTeleport.remove(teleport);
            index = 0;
        } else {
            player.favoriteTeleport.add(teleport);
        }

        isFavorite = player.favoriteTeleport.contains(teleport);
        player.send(new SendConfig(348, isFavorite ? 0 : 1));
        player.send(new SendMessage("You have " + (isFavorite ? "" : "un-") + "favorited the " + teleport.getName() + " teleport."));
        TeleportType type = player.attributes.get("TELEPORT_TYPE_KEY", TeleportType.class);
        open(player, type, index);
    }

    /** Handles special case TELEPORT. */
    public static void special(Player player, Teleport teleport) {
        //TODO : Add birdhouse teleports. (https://oldschool.runescape.wiki/w/Bird_house_trapping#Locations) - Add teleport to fossil island tree, have tree display tele options.
        if (player.isTeleblocked()) {
            player.message("You are currently under the affects of a teleblock spell and can not teleport!");
            return;
        }

        DialogueFactory factory = player.dialogueFactory;

        switch (teleport) {
            case FARMING:
                factory.sendOption("Catherby", () -> {
                    Teleportation.teleport(player, new Position(2805, 3464, 0));
                    player.message(true, "You have teleported to the Catherby farming area.");
                }, "Ardougne", () -> {
                    Teleportation.teleport(player, new Position(2662, 3375, 0));
                    player.message(true, "You have teleported to the Ardougne farming area.");
                }, "Falador", () -> {
                    Teleportation.teleport(player, new Position(3056, 3310, 0));
                    player.message(true, "You have teleported to the Falador farming area.");
                }, "Phasmatys", () -> {
                    Teleportation.teleport(player, new Position(3600, 3524, 0));
                    player.message(true, "You have teleported to the Phasmatys farming area.");
                }).execute();
                break;
            case AGILITY:
                factory.sendOption("Gnome agility course (Level 1 agility req.)", () -> {
                    Teleportation.teleport(player, new Position(2480, 3437, 0));
                    player.send(new SendMessage("You have teleported to the Gnome agility course.", true));
                }, "Barbarian agility course (Level 35 agility req.)", () -> {
                    Teleportation.teleport(player, new Position(2546, 3551, 0));
                    player.send(new SendMessage("You have teleported to the Barbarian agility course.", true));
                }, "Wilderness agility course (Level 49 agility req.)", () -> {
                    Teleportation.teleport(player, new Position(2998, 3915, 0));
                    player.send(new SendMessage("You have teleported to the Wilderness agility course.", true));
                }, "Rooftop courses", () -> {
                    factory.sendStatement("Loading").sendOption("Seer's Village rooftop course (Level 60 agility req.)", () -> {
                        Teleportation.teleport(player, new Position(2729, 3488, 0));
                        player.send(new SendMessage("You have teleported to the Seer's Village rooftop agility course.", true));
                    }, "Ardougne rooftop course (Level 90 agility req.)", () -> {
                        Teleportation.teleport(player, new Position(2674, 3297, 0));
                        player.send(new SendMessage("You have teleported to the Ardougne rooftop agility course.", true));
                    }).execute();
                }, "Nevermind", factory::clear).execute();
                break;
            case MINING:
                factory.sendOption("Varrock", () -> {
                    Teleportation.teleport(player, new Position(3285, 3365, 0));
                    player.send(new SendMessage("You have teleported to the varrock mining area.", true));
                }, "Falador", () -> {
                    Teleportation.teleport(player, new Position(3044, 9785, 0));
                    player.send(new SendMessage("You have teleported to the falador mining area.", true));
                }, "Rune Essence", () -> {
                    Teleportation.teleport(player, new Position(2910, 4832, 0));
                    player.send(new SendMessage("You have teleported to the rune essence mining area.", true));
                }, "Shilo Village", () -> {
                    Teleportation.teleport(player, new Position(2826, 2997, 0));
                    player.send(new SendMessage("You have teleported to the shilo village mining area.", true));
                }, "Nevermind", factory::clear).execute();
                break;
            case RUNECRAFTING:
                factory.sendOption("Abyss", () -> {
                    Teleportation.teleport(player, new Position(3039, 4836, 0));
                    player.send(new SendMessage("You have teleported to the abyss area.", true));
                }, "Astral Altar", () -> {
                    Teleportation.teleport(player, new Position(2155, 3857, 0));
                    player.send(new SendMessage("You have teleported to the astral altar.", true));
                }, "Wrath Altar", () -> {
                    Teleportation.teleport(player, new Position(2335, 4826, 0));
                    player.send(new SendMessage("You have teleported to the wrath altar.", true));
                }, "Ournia Altar", () -> {
                    Teleportation.teleport(player, new Position(2464, 3249, 0));
                    player.send(new SendMessage("You have teleported to the ournia altar.", true));
                }, "Nevermind", factory::clear).execute();
                break;
            case WOODCUTTING:
                factory.sendOption("Camelot", () -> {
                    Teleportation.teleport(player, new Position(2724, 3475, 0));
                    player.send(new SendMessage("You have teleported to the camelot woodcutting area.", true));
                }, "Woodcutting Guild", () -> {
                    Teleportation.teleport(player, new Position(1587, 3488, 0));
                    player.send(new SendMessage("You have teleported to the woodcutting guild.", true));
                }, "Nevermind", factory::clear).execute();
                break;
            case HUNTER:
                factory.sendOption("Puro Puro", () -> {
                    Position[] teleports = {new Position(2619, 4292), new Position(2564, 4292), new Position(2564, 4347), new Position(2619, 4347)};
                    Teleportation.teleport(player, Utility.randomElement(teleports));
                    player.send(new SendMessage("You have teleported to the puro puro hunter area.", true));
                }, "Nevermind", factory::clear).execute();
                break;
            case FISHING:
                factory.sendOption("Catherby", () -> {
                        Teleportation.teleport(player, new Position(2809, 3435, 0));
                        player.send(new SendMessage("You have teleported to the catherby fishing area.", true));
                }, "Fishing Guild", () -> {
                    if(player.skills.getLevel(10) >= 68) {
                        Teleportation.teleport(player, new Position(2594, 3415, 0));
                    player.send(new SendMessage("You have teleported to the fishing guild.", true));
                    } else {
                        player.send(new SendMessage("You need 68 fishing to access the fishing guild."));

                    }
                }, "Nevermind", factory::clear).execute();
                break;
            case GODWARS:
               Teleportation.teleport(player, new Position(2882, 5308, 2), 20, () -> GodwarsActivity.create(player));


              /*  factory.sendOption("General Graardor", () -> {
                    Teleportation.teleport(player, new Position(2864, 5354, 2));
                    player.send(new SendMessage("You have teleported to the General Graardor boss."));
                }, "Commander Zilyana", () -> {
                    Teleportation.teleport(player, new Position(2907, 5265, 0));
                    player.send(new SendMessage("You have teleported to the Commander Zilyana boss."));
                }, "K'ril Tsutsaroth", () -> {
                    Teleportation.teleport(player, new Position(2925, 5331, 2));
                    player.send(new SendMessage("You have teleported to the K'ril Tsutsaroth boss."));
                }, "Kree'arra", () -> {
                    Teleportation.teleport(player, new Position(2839, 5296, 2));
                    player.send(new SendMessage("You have teleported to the Kree'arra boss."));
                }, "Nevermind", factory::clear).execute();*/
                break;
            case WARRIOR_GUILD:
                Teleportation.teleport(player, new Position(2846, 3541, 0), 20, () -> WarriorGuild.create(player));
                break;
            case BARROWS:
                Teleportation.teleport(player, new Position(3565, 3315, 0), 20, () -> Barrows.create(player));
                break;
        }
    }

    /** Gets a list of teleports based off the teleport type. */
    private static List<Teleport> getTeleports(TeleportType type) {
        List<Teleport> list = new ArrayList<>();
        for (Teleport t : Teleport.values()) {
            if (t.getType() == type) list.add(t);
        }
        return list;
    }

    /** Gets the ordinal of a teleport based on the list ordinal. */
    private static int getOrdinal(int button) {
        int base_button = -7484;
        int ordinal = Math.abs((base_button - button) / 2);
        return ordinal;
    }
}
