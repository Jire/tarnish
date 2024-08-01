package com.osroyale.content.skill.impl.crafting.impl;

import com.osroyale.Config;
import com.osroyale.net.packet.out.SendInputAmount;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.game.action.Action;
import com.osroyale.game.action.policy.WalkablePolicy;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.content.dialogue.ChatBoxItemDialogue;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.ItemDefinition;
import com.osroyale.util.Utility;

import java.util.Arrays;
import java.util.Optional;


/**
 * Handles stringing amulets.
 *
 * @author Daniel
 */
public class Stringing {

    /**
     * The amulet data.
     */
    public enum AmuletData {
        GOLD(1673, 1692, 8),
        SAPPHIRE(1675, 1694, 24),
        EMERALD(1677, 1696, 31),
        RUBY(1679, 1698, 50),
        DIAMOND(1681, 1700, 70),
        DRAGONSTONE(1683, 1702, 80),
        ONYX(6579, 6581, 90),
        ZENYTE(19501, 19541, 98);

        /**
         * The amulet item.
         */
        private final int ingredient;

        /**
         * The product item.
         */
        private final int product;

        /**
         * The level required.
         */
        private final int level;

        /**
         * Constructs a new <code>AmuletData</code>.
         *
         * @param ingredient The amulet item.
         * @param product    The product item.
         * @param level      The level required.
         */
        AmuletData(int ingredient, int product, int level) {
            this.ingredient = ingredient;
            this.product = product;
            this.level = level;
        }

        /**
         * Grabs the amulet data.
         *
         * @param ingredient The amulet ingredient.
         * @return The amulet data.
         */
        public static Optional<AmuletData> forAmulet(int ingredient) {
            return Arrays.stream(values()).filter(a -> a.ingredient == ingredient).findAny();
        }
    }

    /**
     * Handles using item.
     *
     * @param player The player instance.
     * @param used   The item being used.
     * @param with   The item being used with.
     */
    public static boolean useItem(Player player, Item used, Item with) {
        if (used.getId() != 1759 && with.getId() != 1759) {
            return false;
        }

        Item wool = used.getId() == 1759 ? used : with;
        Item amulet = wool.getId() == used.getId() ? with : used;

        if (!AmuletData.forAmulet(amulet.getId()).isPresent()) {
            return false;
        }

        AmuletData data = AmuletData.forAmulet(amulet.getId()).get();
        craft(player, data);
        return false;
    }

    /**
     * Handles crafting the amulet.
     *
     * @param player The player instance.
     * @param amulet The amulet data.
     */
    public static void craft(Player player, AmuletData amulet) {
        player.dialogueFactory.clear();

        if (player.skills.getMaxLevel(Skill.CRAFTING) < amulet.level) {
            player.dialogueFactory.sendStatement("You need a crafting level of " + amulet.level + " to string this!").execute();
            return;
        }

        if (!player.inventory.contains(amulet.ingredient) || !player.inventory.contains(1759)) {
            player.dialogueFactory.sendStatement("You do not have the required items to do this!").execute();
            return;
        }

        ChatBoxItemDialogue.sendInterface(player, 1746, amulet.ingredient, 170);
        player.chatBoxItemDialogue = new ChatBoxItemDialogue(player) {
            @Override
            public void firstOption(Player player) {
                player.action.execute(string(player, amulet, 1), false);
            }

            @Override
            public void secondOption(Player player) {
                player.action.execute(string(player, amulet, 5), true);
            }

            @Override
            public void thirdOption(Player player) {
                player.send(new SendInputAmount("Enter amount", 2, input -> player.action.execute(string(player, amulet, Integer.parseInt(input)), true)));
            }

            @Override
            public void fourthOption(Player player) {
                player.action.execute(string(player, amulet, 14), true);
            }
        };
    }

    /**
     * The amulet stringing action.
     *
     * @param player The player instance.
     * @param amulet The amulet data.
     * @param amount The amount beeing spun.
     * @return The spinnable action.
     */
    private static Action<Player> string(Player player, AmuletData amulet, int amount) {
        return new Action<Player>(player, 2, true) {
            int ticks = 0;

            @Override
            public void execute() {
                if (!player.inventory.contains(amulet.ingredient) || !player.inventory.contains(1759)) {
                    cancel();

                    return;
                }

                player.inventory.remove(amulet.ingredient, 1);
                player.inventory.remove(1759, 1);
                player.inventory.add(amulet.product, 1);
                player.skills.addExperience(Skill.CRAFTING, 4 * Config.CRAFTING_MODIFICATION);
                player.send(new SendMessage("You string the " + ItemDefinition.get(amulet.ingredient).getName() + " into " + Utility.getAOrAn(ItemDefinition.get(amulet.product).getName()) + " " + ItemDefinition.get(amulet.product).getName() + "."));

                if (++ticks == amount) {
                    cancel();
                    return;
                }
            }

            @Override
            public String getName() {
                return "Stringing";
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
