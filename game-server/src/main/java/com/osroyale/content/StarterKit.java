package com.osroyale.content;

import com.osroyale.game.world.entity.mob.UpdateFlag;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.items.Item;
import com.osroyale.net.packet.out.SendConfig;
import com.osroyale.net.packet.out.SendItemOnInterface;
import com.osroyale.net.packet.out.SendRunEnergy;
import com.osroyale.net.packet.out.SendString;

import java.util.Arrays;

/**
 * Handles the starter kits.
 *
 * @author Daniel
 */
public class StarterKit {
    /** Handles opening the starter kit interface. */
    public static void open(Player player) {
        player.locking.lock();
        refresh(player, KitData.NORMAL);
        player.interfaceManager.open(45000);
    }

    /** Handles refreshing the starter kit interface. */
    public static void refresh(Player player, StarterKit.KitData kit) {
        player.attributes.set("STARTER_KEY", kit);
        for (int index = 0, string = 45004; index < 4; index++, string += 1) {
            String desc = index >= kit.getDescription().length ? "" : kit.getDescription()[index];
            player.send(new SendString(desc, string));
        }

        player.equipment.clear();
        player.movement.setRunningToggled(true);
        player.send(new SendRunEnergy());
        if (kit.equipment == null) {
            player.equipment.clear();
            player.updateFlags.add(UpdateFlag.APPEARANCE);
        } else {
            Arrays.stream(kit.getEquipment()).forEach(player.equipment::manualWear);
        }

        player.equipment.refresh();
        player.send(new SendConfig(1085, kit.ordinal()));
        player.send(new SendItemOnInterface(45021, kit.getItems()));
    }

    /** Holds the starter kit data. */
    public enum KitData {
        NORMAL(PlayerRight.PLAYER, new String[]{
                "Play Tarnish as a casual player.",
                "This game mode has no restrictions at all.",
                "You will be given a pre-made bank containing welfare equipment."
        }, null,
                new Item(995, 100000)
        ),
        IRONMAN(PlayerRight.IRONMAN, new String[]{
                "Playing as an ironman will restrict you from trading, picking up drops from other",
                "player's kills including pvp or if another player has dealt any damage to a npc,",
                "access to certain shops, access from using the marketplace, and playing certain minigames",
                "which include duel arena. You will have access to ironman armour and a distinct rank. "
        }, new Item[]{new Item(12810), new Item(12812), new Item(12811), new Item(1277), new Item(1173), new Item(4119)},
                new Item(995, 10000), new Item(1351, 1), new Item(590, 1), new Item(303, 1),
                new Item(315, 1), new Item(1925, 1), new Item(1931, 1),
                new Item(2309, 1), new Item(1265, 1), new Item(1205, 1),
                new Item(1277, 1), new Item(1171, 1), new Item(841, 1),
                new Item(882, 250), new Item(556, 250), new Item(558, 250),
                new Item(555, 250), new Item(557, 250), new Item(559, 250)
        ),
        ULTIMATE_IRONMAN(PlayerRight.ULTIMATE_IRONMAN, new String[]{
                "In addition to all the regular ironman rules the following conditions apply as well:",
                "The use of banking (they are still able to use noted items on bank booths to unnote them)",
                "Keep any item on death nor use the Protect Item prayer ",
        }, new Item[]{new Item(12813), new Item(12814), new Item(12815), new Item(1277), new Item(1173), new Item(4119)},
                new Item(995, 10000), new Item(1351, 1), new Item(590, 1), new Item(303, 1),
                new Item(315, 1), new Item(1925, 1), new Item(1931, 1),
                new Item(2309, 1), new Item(1265, 1), new Item(1205, 1),
                new Item(1277, 1), new Item(1171, 1), new Item(841, 1),
                new Item(882, 250), new Item(556, 250), new Item(558, 250),
                new Item(555, 250), new Item(557, 250), new Item(559, 250)
        ),
        HARDCORE_IRONMAN(PlayerRight.HARDCORE_IRONMAN, new String[]{
                "Hardcore Ironman players will only have one life, in addition to the standard restrictions",
                "given to all ironmen. If a hardcore ironman dies, they will be converted to a Ironman",
                "chat badge standard ironman. Safe deaths, such as those in minigames, will not cause",
                "the player to become a standard ironman.",
        }, new Item[]{new Item(20792), new Item(20794), new Item(20796), new Item(1277), new Item(1173), new Item(4119)},
                new Item(995, 10000), new Item(1351, 1), new Item(590, 1), new Item(303, 1),
                new Item(315, 1), new Item(1925, 1), new Item(1931, 1),
                new Item(2309, 1), new Item(1265, 1), new Item(1205, 1),
                new Item(1277, 1), new Item(1171, 1), new Item(841, 1),
                new Item(882, 25), new Item(556, 25), new Item(558, 15),
                new Item(555, 6), new Item(557, 4), new Item(559, 2)
        );

        /** The player right of the starter kit. */
        private final PlayerRight right;

        /** The starter kit description. */
        private final String[] description;

        /** The starter kit equipment items. */
        private final Item[] equipment;

        /** The starter kit items. */
        private final Item[] items;

        /** Constructs a new <code>KitData</code>. */
        KitData(PlayerRight right, String[] description, Item[] equipment, Item... items) {
            this.right = right;
            this.description = description;
            this.equipment = equipment;
            this.items = items;
        }

        public PlayerRight getRight() {
            return right;
        }

        public String[] getDescription() {
            return description;
        }

        public Item[] getEquipment() {
            return equipment;
        }

        public Item[] getItems() {
            return items;
        }

    }

}
