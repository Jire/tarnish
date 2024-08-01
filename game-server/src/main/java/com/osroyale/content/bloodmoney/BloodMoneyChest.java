package com.osroyale.content.bloodmoney;

import com.osroyale.content.puzzle.PuzzleType;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.object.CustomGameObject;
import com.osroyale.net.discord.DiscordPlugin;
import com.osroyale.util.Stopwatch;
import com.osroyale.util.Utility;

/**
 * The blood money chest manager.
 *
 * @author Daniel
 */
public class BloodMoneyChest {
    /** The state of the blood money chest. */
    public static boolean active;

    /** The current blood money chest viewer. */
    private static Player viewer;

    /** The blood money chest object. */
    public static CustomGameObject chest;

    /** The guardian npc. */
    public static Npc guardian;

    /** Blood chest position. */
    public static BloodMoneyPosition data;

    /** The stopwatch for this event. */
    public static final Stopwatch stopwatch = Stopwatch.start();

    /** Handles spawning the blood money chest. */
    public static void spawn() {
        data = Utility.randomElement(BloodMoneyPosition.values());

        chest = new CustomGameObject(27290, data.position);
        chest.rotate(data.direction);
        chest.register();

        guardian = new Npc(8066, data.position);
        guardian.boundaries = Utility.getInnerBoundaries(data.position, 5, 5);
        guardian.walkingRadius = 3;
        guardian.register();

        active = true;

        World.sendMessage("<icon=0><col=FF0000> Blood money chest has spawned at: " + data.name + ".");
        DiscordPlugin.sendSimpleMessage("Blood money chest has spawned at " + data.name + "!");
    }

    /** Handles finishing the blood money chet. */
    public static void finish(boolean unlocked) {
        active = false;
        chest.unregister();
        if (guardian != null && guardian.isRegistered()) {
            guardian.unregister();
        }

        if (viewer != null) {
            if (unlocked) {
                World.sendMessage("<icon=0><col=FF0000> Blood money chest was unlocked by " + viewer.getName() + "!");
                DiscordPlugin.sendSimpleMessage("Blood money chest has been unlocked by " + viewer.getName() + "!");
                viewer.inventory.addOrDrop(new Item(20608));
            }

            viewer.interfaceManager.close(4543);
            viewer = null;
            return;
        }
        World.sendMessage("<icon=0><col=FF0000> Blood money chest has vanished!");
    }

    /** Handles opening the blood money chest. */
    public static void open(Player player) {
        if (guardian != null) {
            player.dialogueFactory.sendStatement("The stone guardian must be killed!").execute();
            return;
        }

        if (viewer != null) {
            if (!viewer.interfaceManager.isInterfaceOpen(4543)) {
                viewer = null;
            } else {
                player.dialogueFactory.sendStatement("There is already someone unlocking the chest!").execute();
                return;
            }
        }

        viewer = player;
        player.puzzle.open(PuzzleType.BLOOD_MONEY);
    }

    /** The information displayed on information tab. */
    public static String getInformation() {
        return !active ? "Not Active" : data.name;
    }
}
