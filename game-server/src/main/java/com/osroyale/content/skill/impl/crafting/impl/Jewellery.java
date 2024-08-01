package com.osroyale.content.skill.impl.crafting.impl;

import com.osroyale.Config;
import com.osroyale.game.Animation;
import com.osroyale.game.action.Action;
import com.osroyale.game.action.policy.WalkablePolicy;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.ItemDefinition;
import com.osroyale.net.packet.out.SendInterfaceConfig;
import com.osroyale.net.packet.out.SendItemOnInterfaceSlot;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.Utility;

import java.util.Arrays;
import java.util.Optional;

/**
 * Handles crafting jewellery.
 *
 * @author Daniel
 */
public final class Jewellery {

    /**
     * The jewellery type.
     */
    public enum JewelleryType {
        RING(4233),
        NECKLACE(4239),
        AMULET(4245);

        /**
         * The itemcontainer identification.
         */
        public final int identification;

        /**
         * Constructs a new <code>JewelleryType</code>.
         *
         * @param identification The itemcontainer identification.
         */
        JewelleryType(int identification) {
            this.identification = identification;
        }
    }

    /**
     * The jewellery data.
     */
    /**
     * Need to add furnace at home.
     */
    public enum JewelleryData {
        GOLD_RING(1635, 5, 15, JewelleryType.RING, new Item(1592), new Item(2357)),
        SAPPHIRE_RING(1637, 20, 40, JewelleryType.RING, new Item(1592), new Item(2357), new Item(1607)),
        EMERALD_RING(1639, 27, 55, JewelleryType.RING, new Item(1592), new Item(2357), new Item(1605)),
        RUBY_RING(1641, 34, 70, JewelleryType.RING, new Item(1592), new Item(2357), new Item(1603)),
        DIAMOND_RING(1643, 43, 85, JewelleryType.RING, new Item(1592), new Item(2357), new Item(1601)),
        DRAGONSTONE_RING(1645, 55, 100, JewelleryType.RING, new Item(1592), new Item(2357), new Item(1615)),
        ONYX_RING(6575, 67, 85, JewelleryType.RING, new Item(1592), new Item(2357), new Item(6573)),

        GOLD_NECKLACE(1654, 6, 20, JewelleryType.NECKLACE, new Item(1597), new Item(2357)),
        SAPPHIRE_NECKLACE(1656, 22, 55, JewelleryType.NECKLACE, new Item(1597), new Item(2357), new Item(1607)),
        EMERALD_NECKLACE(1658, 29, 60, JewelleryType.NECKLACE, new Item(1597), new Item(2357), new Item(1605)),
        RUBY_NECKLACE(1660, 40, 75, JewelleryType.NECKLACE, new Item(1597), new Item(2357), new Item(1603)),
        DIAMOND_NECKLACE(1662, 56, 90, JewelleryType.NECKLACE, new Item(1597), new Item(2357), new Item(1601)),
        DRAGONSTONE_NECKLACE(1664, 72, 105, JewelleryType.NECKLACE, new Item(1597), new Item(2357), new Item(1615)),
        ONYX_NECKLACE(6577, 82, 120, JewelleryType.NECKLACE, new Item(1597), new Item(2357), new Item(6573)),

        GOLD_AMULET(1673, 8, 30, JewelleryType.AMULET, new Item(1595), new Item(2357)),
        SAPPHIRE_AMULET(1675, 24, 65, JewelleryType.AMULET, new Item(1595), new Item(2357), new Item(1607)),
        EMERALD_AMULET(1677, 31, 61, JewelleryType.AMULET, new Item(1595), new Item(2357), new Item(1605)),
        RUBY_AMULET(1679, 50, 85, JewelleryType.AMULET, new Item(1595), new Item(2357), new Item(1603)),
        DIAMOND_AMULET(1681, 70, 100, JewelleryType.AMULET, new Item(1595), new Item(2357), new Item(1601)),
        DRAGONSTONE_AMULET(1683, 80, 125, JewelleryType.AMULET, new Item(1595), new Item(2357), new Item(1615)),
        ONYX_AMULET(6579, 90, 150, JewelleryType.AMULET, new Item(1595), new Item(2357), new Item(6573));

        /**
         * The production item.
         */
        public int product;

        /**
         * The level required.
         */
        public int level;

        /**
         * The experience rewarded.
         */
        public double experience;

        /**
         * The jewellery type.
         */
        public JewelleryType type;

        /**
         * The materials required.
         */
        public Item[] materials;

        /**
         * Constructs a new <code>JewelleryData</code>.
         *
         * @param product    The production item.
         * @param level      The level required.
         * @param experience The experience rewarded.
         * @param type       The jewellery type.
         * @param materials  The materials required.
         */
        JewelleryData(int product, int level, double experience, JewelleryType type, Item... materials) {
            this.product = product;
            this.level = level;
            this.experience = experience;
            this.type = type;
            this.materials = materials;
        }

        /**
         * Gets the size of all the jewellery types.
         *
         * @param type The jewellery type.
         * @return The jewellery size.
         */
        public static int getSize(JewelleryType type) {
            int count = 0;
            for (JewelleryData data : values()) {
                if (data.type == type) {
                    count++;
                }
            }
            return count;
        }

        /**
         * Gets the product items based on the jewellery type.
         *
         * @param type The jewellery type.
         * @return The jewellery type production items.
         */
        public static int[] getItems(JewelleryType type) {
            int[] items = new int[getSize(type)];
            int count = 0;
            for (JewelleryData data : values()) {
                if (data.type == type) {
                    items[count] = data.product;
                    count++;
                }
            }
            return items;
        }

        /**
         * Gets the jewellery data based on the product item.
         *
         * @param item The item being searched.
         * @return The jewellery data.
         */
        public static Optional<JewelleryData> forItem(int item) {
            return Arrays.stream(values()).filter(i -> i.product == item).findAny();
        }
    }

    /**
     * Opens the jewellery creation itemcontainer.
     *
     * @param player The player instance.
     */
    public static void open(Player player) {
        int[] items = JewelleryData.getItems(JewelleryType.RING);
        for (int i = 0; i < items.length; i++) {
            player.send(new SendItemOnInterfaceSlot(JewelleryType.RING.identification, items[i], 1, i));
        }

        items = JewelleryData.getItems(JewelleryType.NECKLACE);
        for (int i = 0; i < items.length; i++) {
            player.send(new SendItemOnInterfaceSlot(JewelleryType.NECKLACE.identification, items[i], 1, i));
        }

        items = JewelleryData.getItems(JewelleryType.AMULET);
        for (int i = 0; i < items.length; i++) {
            player.send(new SendItemOnInterfaceSlot(JewelleryType.AMULET.identification, items[i], 1, i));
        }

        player.send(new SendInterfaceConfig(4229, 0, -1));
        player.send(new SendInterfaceConfig(4235, 0, -1));
        player.send(new SendInterfaceConfig(4241, 0, -1));
        player.interfaceManager.open(4161);
    }

    /**
     * Handles clicking on the itemcontainer.
     *
     * @param player The player instance.
     * @param item   The item being crafted.
     * @param amount The amount being crafted.
     */
    public static void click(Player player, int item, int amount) {
        if (!JewelleryData.forItem(item).isPresent()) {
            return;
        }

        JewelleryData jewellery = JewelleryData.forItem(item).get();

        if (player.skills.getMaxLevel(Skill.CRAFTING) < jewellery.level) {
            String name = ItemDefinition.get(jewellery.product).getName();
            player.send(new SendMessage("You need a crafting level of " + jewellery.level + " to craft " + Utility.getAOrAn(name) + " " + name + "."));
            return;
        }

        boolean contains = true;
        for (Item i : jewellery.materials) {
            if (!player.inventory.contains(i)) {
                String req = i.getName();
                player.send(new SendMessage("You need " + Utility.getAOrAn(req) + " " + req + " to make this!"));
                contains = false;
            }
        }

        if (!contains)
            return;

        player.interfaceManager.close();
        player.action.execute(craft(player, jewellery, amount), true);
    }

    /**
     * The jewellery crafting animation.
     *
     * @param player    The player instance.
     * @param jewellery The jewellery data being crafted.
     * @param amount    The amount of jewellery being crafter.
     * @return The jewellery crafting action.
     */
    private static Action<Player> craft(Player player, JewelleryData jewellery, int amount) {
        return new Action<Player>(player, 4, true) {
            int ticks = 0;

            @Override
            public void execute() {
                if (!player.inventory.containsAll(jewellery.materials)) {
                    player.send(new SendMessage("You do not have the required items to craft this!"));
                    cancel();
                    return;
                }

                player.animate(new Animation(899));

                for (int index = 0; index < jewellery.materials.length; index++) {
                    if (index != 0) {
                        player.inventory.remove(jewellery.materials[index], -1, false);
                    }
                }

                player.inventory.add(new Item(jewellery.product), -1, true);
                player.skills.addExperience(Skill.CRAFTING, jewellery.experience * Config.CRAFTING_MODIFICATION);
                player.send(new SendMessage("You have crafted " + Utility.getAOrAn(new Item(jewellery.product).getName()) + " " + new Item(jewellery.product).getName() + "."));

                if (++ticks == amount) {
                    cancel();
                    return;
                }
            }

            @Override
            public String getName() {
                return "Jewellery crafting";
            }

            @Override
            public boolean prioritized() {
                return false;
            }

            @Override
            public WalkablePolicy getWalkablePolicy() {
                return WalkablePolicy.NON_WALKABLE;
            }
        };
    }
}
